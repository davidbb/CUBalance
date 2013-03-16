package ca.carleton.ccsl.cubalance;

import java.util.Date;

public class CUBalanceResult {
	private Float balance = 0.0f;
	private String error = null;
	private Date date = null;

	public Float getBalance() {
		return balance;
	}

	public void setBalance(Float balance) {
		this.balance = balance;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public boolean hasError() {
		return error != null;
	}

	public String getError() {
		return error;
	}

	public void setError(String error) {
		this.error = error;
	}
}
