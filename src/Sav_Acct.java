// Sav_Acct.java :

public class Sav_Acct extends Account
{
    private double interest_rate;

    // public Sav_Acct (0)
    {
        interest_rate = 5;
    }

    public void calculate_interest (int login_count)
    {
        if (login_count % 5 == 0)
        {
            balance = balance * (1 + interest_rate / 100);
        }
    }
}
