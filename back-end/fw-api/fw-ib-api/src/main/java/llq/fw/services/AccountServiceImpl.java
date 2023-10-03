package llq.fw.services;

import java.util.ArrayList;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import llq.fw.cm.common.Constants.FwError;
import llq.fw.cm.models.Cust;
import llq.fw.cm.models.CustAcc;
import llq.fw.cm.payload.response.BaseResponse;
import llq.fw.cm.security.repository.CustAccRepository;
import llq.fw.cm.security.repository.CustRepository;
import llq.fw.models.Account;

@Component
public class AccountServiceImpl {
	
	@Autowired
	CustRepository custRepository;
	@Autowired
	CustAccRepository custAccRepository;
	public static ArrayList<Account> arrDD = new ArrayList<Account>();
	public static ArrayList<Account> arrFD = new ArrayList<Account>();
	public static ArrayList<Account> arrLN = new ArrayList<Account>();

	public static ArrayList<Account> addDD() {
		Account acc1 = new Account("01234567891", "");
		Account acc2 = new Account("01234567892", "");
		Account acc3 = new Account("01234567893", "");
		Account acc4 = new Account("01234567894", "");
		Account acc5 = new Account("01234567895", "");
		arrDD.add(acc1);
		arrDD.add(acc2);
		arrDD.add(acc3);
		arrDD.add(acc4);
		arrDD.add(acc5);
		return arrDD;
	}
	public static ArrayList<Account> addFD() {	
		Account acc1 = new Account("0123456781", "");
		Account acc2 = new Account("0123456782", "");
		Account acc3 = new Account("0123456783", "");
		Account acc4 = new Account("0123456784", "");
		Account acc5 = new Account("0123456785", "");
		arrFD.add(acc1);
		arrFD.add(acc2);
		arrFD.add(acc3);
		arrFD.add(acc4);
		arrFD.add(acc5);
		return arrFD;
	}
	public static ArrayList<Account> addLN() {	
		Account acc1 = new Account("030012345671", "");
		Account acc2 = new Account("030012345672", "");
		Account acc3 = new Account("030012345673", "");
		Account acc4 = new Account("030012345674", "");
		Account acc5 = new Account("030012345675", "");
		arrLN.add(acc1);
		arrLN.add(acc2);
		arrLN.add(acc3);
		arrLN.add(acc4);
		arrLN.add(acc5);
		return arrLN;
	}
	
	public ArrayList<Account> checkSDd(Cust cust){
		if(cust.getCustAcc() != null) {
			arrDD.forEach(item -> {
				cust.getCustAcc().forEach(element ->{
					if("1".equals(element.getType())  && element.getAcc().equals(item.getAcc())) {
						item.setId(element.getId().toString());
						return;
					}
				});
			});
		}
		return arrDD;
	}
	
	public ArrayList<Account> checkTDd(Cust cust){
		if(cust.getCustAcc() != null) {
			arrDD.forEach(item -> {
				cust.getCustAcc().forEach(element ->{
					if("2".equals(element.getType()) && element.getAcc().equals(item.getAcc())) {
						item.setId(element.getId().toString());
						return;
					}
				});
			});
		}
		return arrDD;
	}
	
	public ArrayList<Account> checkFd(Cust cust){
		if(cust.getCustAcc() != null) {
			arrFD.forEach(item -> {
				cust.getCustAcc().forEach(element ->{
					if(element.getAcc().equals(item.getAcc())) {
						item.setId(element.getId().toString());
						return;
					}
				});
			});
		}
		return arrFD;
	}
	
	public ArrayList<Account> checkLn(Cust cust){
		if(cust.getCustAcc() != null) {
			arrLN.forEach(item -> {
				cust.getCustAcc().forEach(element ->{
					if(element.getAcc().equals(item.getAcc())) {
						item.setId(element.getId().toString());
						return;
					}
				});
			});
	}
		return arrLN;
	}
	
	public BaseResponse getAccDD() {
		BaseResponse baseResponse = new BaseResponse();
		baseResponse.setFwError(FwError.THANHCONG);
		try {
			arrDD.clear();
			addDD();
			baseResponse.setData(arrDD);
		} catch (Exception e) {
			baseResponse.setFwError(FwError.KHONGTHANHCONG);
		}
		return baseResponse;
	}
	
	public BaseResponse getAccLN() {
		BaseResponse baseResponse = new BaseResponse();
		baseResponse.setFwError(FwError.THANHCONG);
		try {
			arrLN.clear();
			addLN();
			baseResponse.setData(arrLN);
		} catch (Exception e) {
			baseResponse.setFwError(FwError.KHONGTHANHCONG);
		}
		return baseResponse;
	}
	
	public BaseResponse getAccFD() {
		BaseResponse baseResponse = new BaseResponse();
		baseResponse.setFwError(FwError.THANHCONG);
		try {
			arrFD.clear();
			addFD();				
			baseResponse.setData(arrFD);
		} catch (Exception e) {
			System.out.println(e.getMessage());
			baseResponse.setFwError(FwError.KHONGTHANHCONG);
		}
		return baseResponse;
	}
	
	public BaseResponse searchSAccDD(Long id) {
		BaseResponse baseResponse = new BaseResponse();
		baseResponse.setFwError(FwError.THANHCONG);
		try {
			arrDD.clear();
			addDD();
			Cust cust = getCust(id);
			baseResponse.setData(checkSDd(cust));
		} catch (Exception e) {
			baseResponse.setFwError(FwError.KHONGTHANHCONG);
		}
		return baseResponse;
	}
	
	public BaseResponse searchTAccDD(Long id) {
		BaseResponse baseResponse = new BaseResponse();
		baseResponse.setFwError(FwError.THANHCONG);
		try {
			arrDD.clear();
			addDD();
			Cust cust = getCust(id);
			baseResponse.setData(checkTDd(cust));
		} catch (Exception e) {
			baseResponse.setFwError(FwError.KHONGTHANHCONG);
		}
		return baseResponse;
	}
	
	public BaseResponse searchAccLN(Long id) {
		BaseResponse baseResponse = new BaseResponse();
		baseResponse.setFwError(FwError.THANHCONG);
		try {
			arrLN.clear();
			addLN();
			Cust cust = getCust(id);
			baseResponse.setData(checkLn(cust));
		} catch (Exception e) {
			baseResponse.setFwError(FwError.KHONGTHANHCONG);
		}
		return baseResponse;
	}
	
	public BaseResponse searchAccFD(Long id) {
		BaseResponse baseResponse = new BaseResponse();
		baseResponse.setFwError(FwError.THANHCONG);
		try {
			arrFD.clear();
			addFD();				
			Cust cust = getCust(id);
			baseResponse.setData(checkFd(cust));
		} catch (Exception e) {
			System.out.println(e.getMessage());
			baseResponse.setFwError(FwError.KHONGTHANHCONG);
		}
		return baseResponse;
	}
	
	public Cust getCust(Long id){
		var custCur = custRepository.findById(id).orElse(null);
		if (custCur == null) {
			return null;
		}else {
			custCur.setCustAcc(custAccRepository.findByCust(custCur));
		}
		return custCur;
	}
	
	
	
}

