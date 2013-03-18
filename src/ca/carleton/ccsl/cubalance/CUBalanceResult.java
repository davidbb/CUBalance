package ca.carleton.ccsl.cubalance;

public class CUBalanceResult
{
  private Float   balance   = 0.0f;
  private String  error     = null;
     
  public Float getBalance()
  {
    return balance;
  }
  
  public void setBalance(Float balance)
  {
    this.balance = balance;
  }
  
  public boolean hasError()
  {
    return error != null;
  }
  
  public String getError()
  {
    return error;
  }
  
  public void setError(String error)
  {
    this.error = error;
  }
}
