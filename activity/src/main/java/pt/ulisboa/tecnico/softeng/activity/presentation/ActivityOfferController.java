package pt.ulisboa.tecnico.softeng.activity.presentation;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import pt.ulisboa.tecnico.softeng.activity.domain.ActivityProvider;
import pt.ulisboa.tecnico.softeng.activity.exception.ActivityException;
import pt.ulisboa.tecnico.softeng.activity.services.local.ActivityInterface;
import pt.ulisboa.tecnico.softeng.activity.services.local.dataobjects.ActivityData;
import pt.ulisboa.tecnico.softeng.activity.services.local.dataobjects.ActivityOfferData;
import pt.ulisboa.tecnico.softeng.activity.services.local.dataobjects.ActivityProviderData;

@Controller
@RequestMapping(value = "/provider/{providerCode}/activity/{activityCode}/offers")
public class ActivityOfferController {
	private static Logger logger = LoggerFactory.getLogger(ActivityOfferController.class);
	private static final boolean DEBUG = true;
	
	@RequestMapping(method = RequestMethod.GET)
	public String showActivityOffers(Model model, @PathVariable String providerCode, @PathVariable String activityCode) {
		logger.info("showActivityOffers provider:{} activity:{}", providerCode, activityCode);
		
		model.addAttribute("providerCode",providerCode);
		model.addAttribute("activityCode", activityCode);
		
		if (DEBUG && ActivityInterface.getActivities(providerCode)==null){
			ActivityProviderData provider = new ActivityProviderData();
			provider.setName("FOOBAR");
			provider.setCode(providerCode);
			ActivityInterface.createActivityProvider(provider);
		}
		
		if (DEBUG && ActivityInterface.getActivityOffers(providerCode, activityCode)==null){
			ActivityData activity = new ActivityData();
			activity.setName("foo");
			activity.setMaxAge(40);
			activity.setMinAge(20);
			activity.setCapacity(4);
			ActivityInterface.createActivity(providerCode, activity);
		}

		List<ActivityOfferData> offersData = ActivityInterface.getActivityOffers(providerCode, activityCode);
		if (offersData == null) {
			List<ActivityData> activities = ActivityInterface.getActivities(providerCode);
			if(activities == null){
				model.addAttribute("error", "Error: there is no provider with the code " + providerCode);
				model.addAttribute("provider", new ActivityProviderData());
				model.addAttribute("providers", ActivityInterface.getActivityProviders());
				return "providers";
			} else {
				model.addAttribute("error", "Error: there is no activity with the code " + activityCode);
				model.addAttribute("activity", new ActivityData());
				model.addAttribute("activities", ActivityInterface.getActivities(providerCode));
				return "activities";
			}
		} else {
			model.addAttribute("offer", new ActivityOfferData());
			model.addAttribute("offers", offersData);
			return "offers";
		}
	}

	@RequestMapping(method = RequestMethod.POST)
	public String submitActivityOffer(Model model, @PathVariable String providerCode, @PathVariable String activityCode,
			@ModelAttribute ActivityOfferData offerData) {
		logger.info("activityOfferSubmit providerCode:{}, activityCode:{}, begin:{}, end:{}, capacity:{}", providerCode, activityCode,
				offerData.getBegin(), offerData.getEnd(), offerData.getCapacity());

		try {
			ActivityInterface.createActivityOffer(providerCode, activityCode, offerData);
		} catch (ActivityException ae) {
			model.addAttribute("error", "Error: it was not possible to create the activity");
			model.addAttribute("offer", offerData);
			model.addAttribute("offers", ActivityInterface.getActivityOffers(providerCode, activityCode));
			return "adventures";
		}

		return "offers";
	}

}
