package co.ke.technovation.entity;

import java.util.HashMap;

public enum PaymentStatus {
	
	INSUFFICIENT_BALANCE(6),WAITING_FOR_OPERATOR_CONFIRMATION(5),NOT_PAYABLE_VIA_MOBILE_MONEY(4), JUST_IN(3),IN_QUEUE(2),PROCESSED_IN_ERROR(1),PROCESSED_SUCCESSFULLY(0);
	
	private final int code;
	
	private PaymentStatus(int code){
		this.code = code;
	}
	
	public int getCode() {
		return code;
	}
	
	
	private static final HashMap<Integer, PaymentStatus> lookup = new HashMap<Integer, PaymentStatus>();
	
	static {
		for (PaymentStatus status : PaymentStatus.values()){
			lookup.put(status.getCode(), status);
		}
	}
	
	public static PaymentStatus get(int code) {
		return lookup.get(code);
	}

}
