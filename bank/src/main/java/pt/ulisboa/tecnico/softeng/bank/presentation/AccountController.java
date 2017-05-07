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
import pt.ulisboa.tecnico.softeng.bank.services.local.dataobjects.AccountData;
import pt.ulisboa.tecnico.softeng.bank.services.local.dataobjects.BankData;
import pt.ulisboa.tecnico.softeng.bank.services.local.dataobjects.BankData.CopyDepth;
import pt.ulisboa.tecnico.softeng.bank.services.local.dataobjects.ClientData;

@Controller
@RequestMapping(value = "/banks/{bankCode}/clients/{clientCode}/accounts")
public class AccountController {
	private static Logger logger = LoggerFactory.getLogger(AccountController.class);

	@RequestMapping(method = RequestMethod.GET)
	public String showAccounts(Model model, @PathVariable String bankCode, @PathVariable String clientCode) {
		logger.info("showAccounts code:{}", clientCode);

		ClientData clientData = BankInterface.getClientDataByCode(bankCode, clientCode, ClientData.CopyDepth.ACCOUNTS);

		if (clientData == null) {
			model.addAttribute("error", "Error: it does not exist a bank with the code " + clientCode);
			model.addAttribute("client", new ClientData());
			model.addAttribute("clients", BankInterface.getClients());
			return "clients";
		} else {
			model.addAttribute("account", new AccountData());
			model.addAttribute("client", clientData);
			model.addAttribute("bankCode",bankCode);
			return "accounts";
		}
	}
	
	@RequestMapping(method = RequestMethod.POST)
	public String submitAccounts(Model model, @PathVariable String bankCode, @ModelAttribute ClientData clientData,@ModelAttribute AccountData accountData, @PathVariable String clientCode) {
		logger.info("accountSubmit clientCode:{}, iban:{}, balance:{}", clientCode, accountData.getIban(), accountData.getBalance());

		try {
			BankInterface.createClient(bankCode, clientData);
		} catch (BankException be) {
			model.addAttribute("error", "Error: it was not possible to create the client");
			model.addAttribute("account", accountData);
			model.addAttribute("client", BankInterface.getClientDataByCode(bankCode, clientCode, ClientData.CopyDepth.ACCOUNTS));
			return "accounts";
		}

		return "redirect:/banks/" + bankCode + "/clients/" + clientCode + "/accounts";
	}
}

