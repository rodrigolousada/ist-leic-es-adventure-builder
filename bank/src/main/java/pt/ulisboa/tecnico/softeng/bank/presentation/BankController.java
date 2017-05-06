package pt.ulisboa.tecnico.softeng.bank.presentation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import pt.ulisboa.tecnico.softeng.bank.exception.BankException;
import pt.ulisboa.tecnico.softeng.bank.services.local.BankInterface;
import pt.ulisboa.tecnico.softeng.bank.services.local.dataobjects.BankData;

@Controller
@RequestMapping(value = "/banks")
public class BankController {
	private static Logger logger = LoggerFactory.getLogger(BankController.class);

	@RequestMapping(method = RequestMethod.GET)
	public String bankForm(Model model) {
		logger.info("bankForm");
		model.addAttribute("bank", new BankData());
		model.addAttribute("banks", BankInterface.getBanks());
		return "banks";
	}

	@RequestMapping(method = RequestMethod.POST)
	public String bankSubmit(Model model, @ModelAttribute BankData bankData) {
		logger.info("bankSubmit name:{}, code:{}", bankData.getName(), bankData.getCode());

		try {
			BankInterface.createBank(bankData);
		} catch (BankException be) {
			be.printStackTrace();
			model.addAttribute("error", "Error: it was not possible to create the bank");
			model.addAttribute("bank", bankData);
			model.addAttribute("banks", BankInterface.getBanks());
			return "banks";
		}

		return "redirect:/banks";
	}
}

