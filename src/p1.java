/*
 * Name: p1.java
 * Desc: This program will emulate an online banking system that has seven basic
 * operations (create,open,close,deposit,withdraw,transfer,get balance).
 */

public class p1
{
    public static void main (String[] args)
    {
        final int ACTION_ADMIN = 0;
        final int ACTION_CREATE = 1;
        final int ACTION_OPEN = 2;
        final int ACTION_DEPOSIT = 3;
        final int ACTION_WITHDRAW = 4;
        final int ACTION_TRANSFER = 5;
        final int ACTION_ACCOUNT_INFO = 6;
        final int ACTION_CLOSE = 7;
        final int ACTION_EXIT = 9;


        ConsoleReader console = new ConsoleReader (System.in);

        Atm atm = new Atm ();

        while (true)
        {
            int action = -1;
            System.out.println ("\nWelcome to the CS49J Banking System\n");
            System.out.println (ACTION_CREATE + ") Create Customer");
            System.out.println (ACTION_OPEN + ") Open Account");
            System.out.println (ACTION_DEPOSIT + ") Deposit");
            System.out.println (ACTION_WITHDRAW + ") Withdraw");
            System.out.println (ACTION_TRANSFER + ") Transfer");
            System.out.println (ACTION_ACCOUNT_INFO + ") Account Information");
            System.out.println (ACTION_CLOSE + ") Close Account");
            System.out.println (ACTION_EXIT + ") Exit ATM\n");

            System.out.print ("Enter choice ==> ");
            try 
            {
                action = console.readInt ();
            }
            catch (NumberFormatException e) 
            {
                System.out.println ("Please enter a digit.");            
             }
            switch (action)
            {
                case ACTION_ADMIN:
                {
                    atm.admin ();
                    break;
                }

                case ACTION_CREATE:
                {
                    atm.create_customer ();
                    break;
                }

                case ACTION_OPEN:
                {
                    atm.open_account ();
                    break;
                }

                case ACTION_DEPOSIT:
                {
                    atm.deposit ();
                    break;
                }

                case ACTION_WITHDRAW:
                {
                    atm.withdraw ();
                    break;
                }

                case ACTION_TRANSFER:
                {
                    atm.transfer ();
                    break;
                }

                case ACTION_ACCOUNT_INFO:
                {
                    atm.account_info ();
                    break;
                }

                case ACTION_CLOSE:
                {
                    atm.close_account ();
                    break;
                }

                case ACTION_EXIT:
                {
                    System.out.println ("\n Good bye\n");
                    return;
                }
            }
        }
    }
}
