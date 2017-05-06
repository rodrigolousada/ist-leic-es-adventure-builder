package pt.ulisboa.tecnico.softeng.bank.presentation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import pt.ulisboa.tecnico.softeng.bank.exception.BankException;
import pt.ulisboa.tecnico.softeng.bank.services.local.BankInterface;
import pt.ulisboa.tecnico.softeng.bank.services.local.dataobjects.BankData;
import pt.ulisboa.tecnico.softeng.bank.services.local.dataobjects.BankData.CopyDepth;
import pt.ulisboa.tecnico.softeng.bank.services.local.dataobjects.ClientData;

@Controller
@RequestMapping(value = "/banks/{bankCode}/clients")
public class ClientController {
	private static Logger logger = LoggerFactory.getLogger(ClientController.class);

	@RequestMapping(method = RequestMethod.GET)
	public String showClients(Model model, @PathVariable String bankCode) {
		logger.info("showClients code:{}", bankCode);

		BankData bankData = BankInterface.getBankDataByCode(bankCode, CopyDepth.CLIENTS);

		if (bankData == null) {
			model.addAttribute("error", "Error: it does not exist a bank with the code " + bankCode);
			model.addAttribute("bank", new BankData());
			model.addAttribute("banks", BankInterface.getBanks());
			return "banks";
		} else {
			model.addAttribute("client", new ClientData());
			model.addAttribute("bank", bankData);
			return "clients";
		}
	}
	
	@RequestMapping(method = RequestMethod.POST)
	public String submitClients(Model model, @PathVariable String bankCode, @ModelAttribute ClientData clientData) {
		logger.info("clientSubmit bankCode:{}, id:{}, name:{}", bankCode, clientData.getID(), clientData.getName());

		try {
			BankInterface.createClient(bankCode, clientData);
		} catch (BankException be) {
			model.addAttribute("error", "Error: it was not possible to create the client");
			model.addAttribute("client", clientData);
			model.addAttribute("bank", BankInterface.getBankDataByCode(bankCode, CopyDepth.CLIENTS));
			return "clients";
		}

		return "redirect:/banks/" + bankCode + "/clients";
	}
}

