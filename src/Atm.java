
// Atm.java

import java.io.*;
import java.text.NumberFormat;
import java.util.*;


public class Atm
{
    private ArraySort<Customer> cust_ArrayList;
    private int                 starting_account_number;
    private int                 starting_customer_number;
    private String              admin_pin;
    @SuppressWarnings("unused")
    private int                 interest_rate;
    private int                 transaction_counter;     	// to count log-in
                                                         	// from any
                                                         	// customers for
                                                         	// saving interest
                                                         	// calculation

    // variables created by Roxie
    private Customer customer;
    private Account  account;
    private int      customer_count;
    private int      account_count;
    ConsoleReader    console = new ConsoleReader (System.in);

    public Atm () // constructor
    {
        cust_ArrayList = new ArraySort<Customer> (100);
        admin_pin = new String ("abcd"); // Same as admin_pin = "abcd";

        // System.out.println("\nAdmin PIN: " + admin_pin + "\n");
        // System.out.println("\nAdmin PIN length: " + admin_pin.length() +
        // "\n");

        starting_account_number = 1001;
        starting_customer_number = 101;
        interest_rate = 5;

        read_customer_info_from_file ();
    }

    /*
     * Call all methods to get the information needed for creating customers
     */
    public void create_customer ()
    {
        customer = new Customer ();
        get_customer_name ();
        create_new_pin ();
        create_customer_id ();
        add_to_array ();
        update_interest ();
        write_customer_info_to_file ();

        System.out.println (customer);
    }

    /*
     * prompt an entry from customer for the name
     */
    public void get_customer_name ()
    {
        System.out.println ("\nPlease enter your name: ");
        String name_entry = console.readLine ();
        customer.set_name (name_entry);
        // calls get_customer_name method in Customer.java class and return the
        // name value entered
    }

    /*
     * prompt a pin from
     *
     */
    public void create_new_pin ()
    {
        int len = 0;
        String pin_entry = null;
        while (len > 4 || len < 4 || pin_entry.matches ("0000"))
        {
            System.out.println ("Please choose four digits PIN: ");
            pin_entry = console.readLine ();
            len = pin_entry.length ();

            if (pin_entry.matches ("0000"))
            {
                System.out.println ("[0000] cannot be used.\n");
            }

            else if (len > 4 || len < 4)
            {
                System.out.println ("Void PIN is four digits.\n");
            }
        }

        customer.set_pin (pin_entry);
        // calls create_new_pin method in Customer.java class and return the pin
        // value generated

        ++transaction_counter;
        // Increment transaction_counter for the first time a new customer log
        // in
    }

    /*
     * generate the customer id
     */
    public void create_customer_id ()
    {
        int generated_id = starting_customer_number + customer_count;
        // adding the # of existing customer to the starting id number
        // System.out.println("starting_customer_number + counter_string " +
        // generated_id);

        customer_count++;
        // this counter increments every time we create the
        // customer id

        String id_string = String.valueOf (generated_id);
        // converting the id (integer) to string so we can return the string
        // value to Customer.java class

        customer.set_id (id_string);
        // calls set_id method in Customer.java
        // class and return the id value generated
    }

    /*
     * add newly created customer to the array
     */
    public void add_to_array ()
    {
        cust_ArrayList.add (customer);
    }

    /*
     * Call all methods to get the information needed for opening accoutns
     */
    public void open_account ()
    {
        if (0 == cust_ArrayList.size ())
        {
            System.out.println ("\nWe don't have any customer yet.");
            return;
        }

        validate_id ();
        validate_pin ();
        generate_acc ();
        update_interest ();
        write_customer_info_to_file ();
        account.show_account_info ();
    }

    /*
     * Prompt id entry and validating the pin entered with id Used to open
     * account; thus a customer needs to be created before creating account
     */
    public void validate_id ()
    {

        customer = null;
        int id_len = 0;
        String id_entry = "000";

        boolean id_not_found = true;
        // Used for while loop to find the customer ID from ArrayList; will be
        // set to false once we find the matching ID

        int i;
        // Used to increment the array until we find the matched
        // customer ID

        String id_from_class = null;
        // Used to hold the id value returned from Customer Class

        while (id_not_found)

        {
            i = 0;
            // Initialize counter and reset the counter to start over while loop
            // (B)

            // while loop - (A) Limit the entry to three digits.
            while (id_len > 3 || id_len < 3)
            {
                System.out.println ("Please enter your three digit ID:");
                id_entry = console.readLine ();
                id_len = id_entry.length ();

                if (id_len > 3 || id_len < 3)
                {
                    System.out.println ("Valid ID is three digits.\n");
                }
            }

            // While loop (B) --- loop to find the matched customer based on the
            // ID entered by customer
            while (i < cust_ArrayList.size ())
            {
                Customer possible_customer = cust_ArrayList.get (i);
                id_from_class = possible_customer.get_id ();

                if (id_from_class.equals (id_entry))
                {
                    id_not_found = false;
                    customer = possible_customer;
                    break;
                }

                else
                {
                    if (cust_ArrayList.size () == i + 1)
                    {
                        System.out.println ("We cannot locate your ID number. Please try again.\n");
                        id_len = 0;
                        // reset ID length to start over while loop (A)
                    }
                    i++;
                }
            }
        }
    }

    public void validate_pin ()
    {
        int pin_len = 0;
        String pin_entry = "0000";
        boolean pin_not_found = true;
        String pin_from_class = null;

        pin_from_class = customer.get_pin ();

        while (pin_not_found)
        {
            pin_len = 0;

            // Prompt customer to enter four digits PIN. Limit the entry
            // to four digits.
            while (pin_len > 4 || pin_len < 4)
            {
                System.out.println ("Please enter your four digit PIN:");
                pin_entry = console.readLine ();
                pin_len = pin_entry.length ();

                if (pin_len > 4 || pin_len < 4)
                {
                    System.out.println ("Valid PIN is four digits.\n");
                }
            }

            if (pin_from_class.equals (pin_entry))
            {
                pin_not_found = false;
            }
        }
        ++transaction_counter;
        // Increment transaction counter everytime a customer logs in.
        // This method is called everytime s/he logs in.
    }

    /*
     * generate the customer account number
     */
    public void generate_acc ()
    {

        String account_choice;
        // variable to hold the user's account choice

        int generated_num;
        // validation of user accout type choice
        boolean validated = true;
        do
        {
            System.out.println ("Which account would you like to open?\n" + "S) Saving Account	C) Checking Account  ");
            account_choice = console.readLine ();

            if (!account_choice.equalsIgnoreCase ("C") && !account_choice.equalsIgnoreCase ("S"))
            {
                System.out.println ("\nPlease enter (S) or (C)\n");
            }

            else
            {
                validated = false;
            }
        }
        while (validated);

        // If a customer choose Checking, we create Account object
        if (account_choice.equalsIgnoreCase ("c"))
        {
            account = new Account ();
        }

        else
        {
            account = new Sav_Acct ();
        }

        generated_num = starting_account_number + account_count;
        // adding the # of existing customer to the starting id number

        String num_string = String.valueOf (generated_num);
        // converting the id (integer) to string so we can return the string
        // value to Customer.java class

        account.set_acct_num (num_string); // calls set_id method in
        // Customer.java
        // class and return the id value
        // generated
        account.set_account_type (account_choice);

        customer.add_account (account);
        account_count++;
    }

    public boolean update_interest ()
    {
        int size = 0;
        int inactive_accounts = 0;

        int cust_size = cust_ArrayList.size ();
        // if (cust_size == 0)
        // {
        // return false;
        // }

        for (int j = 0; j < cust_size; j++)
        {
            //ArraySort<Account> account_list = cust_ArrayList.get (j).get_all_accounts ();
            ArraySort<Account> account_list = cust_ArrayList.get (j).get_all_accounts ();
            size = account_list.size ();

            if (size == 0)
            {
                break;
            }
            else
            {
                Account account_temp = null;

                for (int i = 0; i < size; i++)
                {
                    account_temp = account_list.get (i);

                    String account_type = account_temp.get_account_type ();

                    if (customer == account_temp.get_customer () && account_type == null)
                    {
                        inactive_accounts++;
                    }

                    if (account_type != null && account_type.equalsIgnoreCase ("saving"))
                    {
                        ((Sav_Acct) account_temp).calculate_interest (transaction_counter);
                    }
                }
            }

        }

        size = customer.get_all_accounts ().size ();

        if (size - inactive_accounts > 0)
        {
            return true;
        }
        else
        {
            return false;
        }

    }

    public void deposit ()
    {
        if (0 == cust_ArrayList.size ())
        {
            System.out.println ("\nWe don't have any customer yet.");
            return;
        }

        validate_id ();
        validate_pin ();

        // updating the interest right after a customer logged in successfully
        boolean validate = update_interest ();

        if (validate != false)
        {
            transaction_deposit ();
            write_customer_info_to_file ();
            account.show_account_info ();
        }

        else
        {
            System.out.println ("\nYou don't have any account with us. ");
        }

    }

    public void transaction_deposit ()
    {

        ArraySort<Account> account_list = new ArraySort<Account> ();
        account_list = customer.get_all_accounts ();
        int size = account_list.size ();

        String existing_account = null;
        String account_type = null;
        String deposit_to = null;

        double deposit_amount = 0;

        if (size == 0)
        {
            System.out.println ("You don't have any accounts with us.");
            return;
        }

        // To show the existing accounts that a customer set up
        // So that s/he can choose the account to be deposited
        boolean found_1st = false;
        for (int i = 0; i < size; i++)
        {
            existing_account = account_list.get (i).get_account_number ();

            if (existing_account != null)
            {
                if (!found_1st)
                {
                    System.out.println ("You have following accounts with us.\n");
                    found_1st = true;
                }
                account_type = account_list.get (i).get_account_type ();
                System.out.println ("Account number : " + existing_account + " (" + account_type + ") ");
            }
        }

        boolean valid = false;

        int j;
        int invalid_counter = 0;
        do // Asking user which account s/he wants to deposit
 // Find the account number in the system
 // Validation for numbers of digits
        {

            // Prompt customer to enter four digits account#. Limit the entry
            // to four digits.
            int length = 0;
            while (length > 4 || length < 4)
            {
                System.out.println ("\nDeposit to : (Choose Account Nubmer) ");
                deposit_to = console.readLine ();
                System.out.println ("");
                length = deposit_to.length ();

                if (length > 4 || length < 4)
                {
                    System.out.println ("Account number is four digits.\n");
                }
            }

            j = 0;
            while (j < size)
            {
                existing_account = account_list.get (j).get_account_number ();

                if (existing_account != null && existing_account.equals (deposit_to))
                {
                    valid = true;
                    break;
                }

                else
                {
                    if (existing_account == null)
                    {
                        @SuppressWarnings("unused")
                        String invalid_account = account_list.get (j).get_null_account_number ();
                        // System.out.println ("\nAccount number " +
                        // invalid_account + " has been closed.");
                        invalid_counter++;
                    }

                    if (j == size - 1)
                    {
                        System.out.println ("We cannot locate the account number. \n" + "Please try again.\n");
                    }

                    j++;

                }
            }

        }
        while (!valid);

        // line 385 increments the counter every time we have null return.
        // if this counter -1 is same as the size, then all the accounts are
        // closed.
        // So we can just exsit this method.

        if (invalid_counter - 1 == size) { return; }

        System.out.println ("\n" + "Deposit Amount : ");
        deposit_amount = console.readDouble ();

        account = account_list.get (j);

        double balance = account_list.get (j).get_balance ();
        balance = balance + deposit_amount;
        account_list.get (j).update_balance (balance);

    }

    public void withdraw ()
    {
        if (0 == cust_ArrayList.size ())
        {
            System.out.println ("\nWe don't have any customer yet.");
            return;
        }

        validate_id ();
        validate_pin ();
        boolean valid = update_interest ();

        if (valid != false)
        {
            transaction_withdraw ();
            write_customer_info_to_file ();
            account.show_account_info ();
        }

        else
        {
            System.out.println ("You don't have any account with us. ");
        }
    }

    public void transaction_withdraw ()
    {
        ArraySort<Account> account_list = new ArraySort<Account> ();
        account_list = null;
        account_list = customer.get_all_accounts ();

        // updating the interest before we show the balances to a customer
        int size = account_list.size ();

        String existing_account = null;
        String account_type = null;
        String withdraw_from = null;

        boolean validated = true;
        boolean found_1st = false;
        // To show the existing accounts that a customer set up
        // So that s/he can choose the account to be deposited
        for (int i = 0; i < size; i++)
        {
            existing_account = account_list.get (i).get_account_number ();
            account_type = account_list.get (i).get_account_type ();
            if (existing_account != null)
            {
                if (!found_1st)
                {
                    System.out.println ("\nYou have following accounts with us.\n");
                    found_1st = true;
                }
                System.out.println ("Account number : " + existing_account + " (" + account_type + ")");
            }
        }

        int withdraw_index;
        do // Asking user which account s/he wants to withdraw
 // Find the account number in the system
 // Validation for numbers of digits
        {

            // Prompt customer to enter four digits account#. Limit the entry
            // to four digits.
            int length = 0;
            while (length > 4 || length < 4)
            {
                System.out.println ("");
                System.out.println ("Withdraw from? : (Choose Account Nubmer) ");
                withdraw_from = console.readLine ();
                length = withdraw_from.length ();

                if (length > 4 || length < 4)
                {
                    System.out.println ("Account number is four digits.\n");
                }
            }

            withdraw_index = 0;
            while (withdraw_index < size)
            {
                existing_account = account_list.get (withdraw_index).get_account_number ();

                if (existing_account != null && existing_account.equals (withdraw_from))
                {
                    validated = false;
                    break;
                }

                else if (existing_account == null)
                {
                    String invalid_account = account_list.get (withdraw_index).get_null_account_number ();

                    if (withdraw_from.equals (invalid_account))
                    {
                        // System.out.println ("Account number " +
                        // invalid_account + " has been closed.");
                        return;
                    }

                    withdraw_index++;

                }

                else
                {
                    if (withdraw_index == size - 1)
                    {
                        System.out.println ("We cannot locate the account number. \n" + "Please try again.\n");

                    }

                    withdraw_index++;
                }
            }

        }
        while (validated);

        account = account_list.get (withdraw_index);
        double balance = account.get_balance ();
        double withdraw_amount = 0;

        do
        {
            System.out.println ("Withdraw amount: ");
            withdraw_amount = console.readDouble ();
            System.out.println ("");

            if (balance < withdraw_amount)
            {
                System.out.println ("The transfer limit is " + balance);
            }

        }
        while (balance < withdraw_amount);

        balance -= withdraw_amount;
        account_list.get (withdraw_index).update_balance (balance);

    }

    public void printout_accouts ()
    // this method creates arrayList from Account class
    // to hold customer's all account information
    {
        ArraySort<Account> account_list = new ArraySort<Account> ();
        account_list = customer.get_all_accounts ();

        int j = account_list.size ();
        for (int i = 0; i < j; i++)
        {
            account_list.get (i).show_account_info ();
        }
    }

    public void account_info ()
    {
        if (0 == cust_ArrayList.size ())
        {
            System.out.println ("\nWe don't have any customer yet.");
            return;
        }

        validate_id ();
        validate_pin ();
        boolean valid = update_interest ();

        if (valid != false)
        {
            printout_accouts ();
        }

        else
        {
            System.out.println ("You don't have any account with us.");
        }
    }

    public void transfer ()
    {
        if (0 == cust_ArrayList.size ())
        {
            System.out.println ("\nWe don't have any customer yet.");
            return;
        }

        validate_id ();
        validate_pin ();
        boolean valid = update_interest ();

        if (valid == true)
        {
            transaction_transfer ();
            write_customer_info_to_file ();
        }

        else
        {
            System.out.println ("You don't have any account with us.");
        }

    }

    public void transaction_transfer ()
    {
        ArraySort<Account> account_list = new ArraySort<Account> ();
        account_list = null;
        account_list = customer.get_all_accounts ();

        System.out.println ("Current balance : ");

        int size = account_list.size ();
        for (int i = 0; i < size; i++)
        {
            account_list.get (i).show_account_info ();
        }

        String existing_account;
        String account_from = null;
        String account_to = null;
        boolean validated = true;

        int from_index = 0;
        // To hold the Account array index where user specified as transfer from
        int to_index = 0;
        // To hold the Account array index where user specified as transfer to

        // To show the existing accounts that a customer set up
        // So that s/he can choose the accout from

        do // Asking user transfer *from * account.
 // Validation of entry so only S or C will be accepted
        {

            // Prompt customer to enter four digits account#. Limit the entry
            // to four digits.

            int length = 0;

            while (length > 4 || length < 4)
            {
                System.out.println ("\n" + "\nTransfer from? : (Please select an account)");
                account_from = console.readLine ();
                length = account_from.length ();

                if (length > 4 || length < 4)
                {
                    System.out.println ("Account number is four digits.\n");
                }
            }

            from_index = 0;
            while (from_index < size)
            {
                existing_account = account_list.get (from_index).get_account_number ();

                if (existing_account != null && existing_account.equals (account_from))
                {
                    validated = false;
                    break;
                }

                else
                {
                    if (from_index == size - 1)
                    {
                        System.out.println ("");
                        System.out.println ("We cannot locate the account number. \n" + "Please try again.\n");
                        from_index++;
                    }

                    else
                    {
                        from_index++;
                    }
                }
            }
        }
        while (validated);

        validated = true;

        do // Asking user which account s/he wants to transfer *to*
        {
            System.out.println ("\n" + "\nTransfer to? : (Please select an account)");

            // Prompt customer to enter four digits account#. Limit the entry
            // to four digits.
            int length = 0;
            while (length > 4 || length < 4)
            {

                account_to = console.readLine ();
                length = account_to.length ();

                if (length > 4 || length < 4)
                {
                    System.out.println ("Account number is four digits.\n");
                }
            }

            to_index = 0;
            while (to_index < size)
            {
                existing_account = account_list.get (to_index).get_account_number ();

                if (existing_account != null && existing_account.equals (account_to))
                {
                    validated = false;
                    break;
                }

                else
                {
                    if (to_index == size - 1)
                    {
                        System.out.println ("");
                        System.out.println ("We cannot locate the account number. \n" + "Please try again.\n");
                        to_index++;
                    }

                    else
                    {
                        to_index++;
                    }
                }
            }
        }
        while (validated);

        double balance_from = 0;
        double balance_to = 0;

        balance_from = account_list.get (from_index).get_balance ();
        balance_to = account_list.get (to_index).get_balance ();

        // validate the transfer amount so that user cannot transfer
        // more than what s/he has

        double transfer_amount = 0;

        if (!account_to.equalsIgnoreCase (account_from))
        {
            do
            {
                System.out.println ("\n" + "Please enter the transfer amount: \n");
                transfer_amount = console.readDouble ();

                if (balance_from < transfer_amount)
                {
                    System.out.println ("The transfer limit is " + balance_from);
                }

            }
            while (balance_from < transfer_amount);

            // Calculate balance after transfer
            balance_from -= transfer_amount;
            balance_to += transfer_amount;
            account_list.get (from_index).update_balance (balance_from);
            account_list.get (to_index).update_balance (balance_to);

            System.out.println ("\n" + "Updated balance : ");

            for (int i = 0; i < size; i++)
            {
                account_list.get (i).show_account_info ();
            }
        }

        else
        {
            System.out.println ("We can't transfer between the same account.");
        }
    }

    public void close_account ()
    {
        if (0 == cust_ArrayList.size ())
        {
            System.out.println ("\nWe don't have any customer yet.");
            return;
        }

        validate_id ();
        validate_pin ();
        boolean valid = update_interest ();

        if (valid == true)
        {
            close_account_transaction ();
            write_customer_info_to_file ();
        }

        else
        {
            System.out.println ("You don't have any account with us.");
        }

    }

    public void close_account_transaction ()
    {
        ArraySort<Account> account_list = new ArraySort<Account> ();
        account_list = customer.get_all_accounts ();

        System.out.println ("Close your account?" + "\n");
        System.out.println ("YES/NO");

        boolean match = false;
        String answer = null;

        do
        {

            answer = console.readLine ();

            if (answer.equalsIgnoreCase ("yes") || answer.equalsIgnoreCase ("no"))
            {
                match = false;
            }
            else
            {
                System.out.println ("Please enter :YES/NO");
            }

        }
        while (match);

        String user_answer = null;
        String existing_account;
        int close_index;
        int size = account_list.size ();
        int null_counter = 0;

        boolean validated = false;

        switch (answer)
        {
            case "yes":
            case "Yes":
            case "YES":
            case "yEs":
            case "yeS":
            case "yES":
            {
                System.out.println ("\n" + "You have the following accounts with us.");
                for (int i = 0; i < size; i++)
                {
                    account_list.get (i).show_account_info ();
                }

                do // Asking user which account s/he wants to close
                {
                    System.out.println ("\n" + "Which account would you like to close? ");

                    // Prompt customer to enter four digits account#. Limit the
                    // entry
                    // to four digits.
                    int length = 0;
                    while (length > 4 || length < 4)
                    {

                        user_answer = console.readLine ();
                        length = user_answer.length ();

                        if (length > 4 || length < 4)
                        {
                            System.out.println ("Account number is four digits.\n");
                        }
                    }

                    close_index = 0;
                    while (close_index < size)
                    {
                        existing_account = account_list.get (close_index).get_account_number ();

                        if (existing_account != null && user_answer.equals (existing_account))
                        {
                            validated = true;
                            break;
                        }

                        if (existing_account == null)
                        {
                            String null_account = account_list.get (close_index).get_null_account_number ();
                            if (user_answer.equals (null_account))
                                System.out.println ("Account number : " + null_account + "(closed)");
                            null_counter++;
                            close_index++;
                        }

                        else
                        {
                            if (close_index == size - 1)
                            {
                                System.out.println ("");
                                System.out.println ("We cannot locate the account number. \n" + "Please try again.\n");
                            }

                            close_index++;
                        }
                    }

                    // Line 845 increments null_counter every time we find
                    // closed
                    // account.
                    // If all accounts are closed, then null_counter is same as
                    // size
                    // Then we get out of the method
                    if (null_counter == size)
                    {
                        System.out.println ("All of your accounts are closed.");
                        return;
                    }

                }
                while (!validated);

                account_list.get (close_index).close_account ();
                break;
            }

            case "NO":
            case "no":
            case "No":
            case "nO":
            {
                System.out.println ("We value you as our customers.\n Please let us know how we can help you.\n");
                break;
            }
        }
    }

    public void admin ()
    {
        admin_validate_pin ();
        admin_transaction ();
    }

    public void admin_validate_pin ()
    {
        int pin_len = 0;
        String pin_entry = "0000";
        boolean found = false;

        while (!found)
        {
            pin_len = 0;

            // Prompt admin to enter four digits PIN. Limit the entry
            // to four digits.
            while (pin_len > 4 || pin_len < 4)
            {
                System.out.println ("Please enter your admin PIN:");
                pin_entry = console.readLine ();
                pin_len = pin_entry.length ();

                if (pin_len > 4 || pin_len < 4)
                {
                    System.out.println ("Valid PIN is four digits.\n");
                }
            }

            if (admin_pin.equals (pin_entry))
            {
                found = true;
            }
        }

    }

    public void admin_transaction ()
    {
        final int DISPLAY_ABC = 1;
        final int DISPLAY_HIGHEST = 2;
        final int DISPLAY_ACCOUNT = 3;
        int user_choice = 0;
        int display_choice = 0;

        boolean valid_entry_1 = false;
        boolean valid_entry_2 = false;

        while (!valid_entry_1)
        {
            user_choice = 0;
            display_choice = 0;

            System.out.println ();
            System.out.println (DISPLAY_ABC + ") See all the accounts in alphabetical order based on the customer name.");
            System.out.println (DISPLAY_HIGHEST + ") See all the accounts in order of highest balance to lowest balance.");
            System.out.println (DISPLAY_ACCOUNT + ") See all the accounts belonging to the same customer ID.");


            System.out.print ("\nEnter choice ==> ");
            try {

                user_choice = console.readInt ();
                valid_entry_1 = true;

            }
            catch (NumberFormatException e1){
                System.out.println ("Please enter a digit.");

            }
        }
        while (!valid_entry_2)
        {
            try
            {
                System.out.println ("\nShow\n 1) Only active accounts\n 2) All accounts including closed accounts");
                System.out.print ("\nEnter choice ==> ");
                display_choice = console.readInt ();
                valid_entry_2 = true;
            }
            catch (NumberFormatException e2){
                System.out.println ("Please enter a digit.");
            }
        }

            switch (user_choice)
            {
                // 1. Display all the accounts in alphabetical order based on the
                // customer name.

                case DISPLAY_ABC:
                {
                    admin_show_accounts_by_name (display_choice);

                    break;
                }

                    // 2. Display all the accounts in order of highest balance to
                    // lowest
                    // balance.
                case DISPLAY_HIGHEST:
                {
                    admin_show_accounts_by_balance (display_choice);
                    break;
                }

                    // 3. Display all the accounts belonging to the same customer
                    // ID.
                case DISPLAY_ACCOUNT:
                {
                    admin_show_customer_accounts ();
                    break;
                }
            }

    }

    public void admin_show_accounts_by_name (int DISPLAY_ACTIVE)
    {
        if (0 == cust_ArrayList.size ())
        {
            System.out.println ("\nWe don't have any customer yet.");
            return;
        }

        System.out.println ("\n[Name]\t[ID]\t[Account#]\t[Pin#]\t[Balance]\n");

        // Sort customers by their names
        cust_ArrayList.sort (null);

        // Look at all customers
        int c_size = cust_ArrayList.size ();
        for (int c = 0; c < c_size; c++)
        {
            Customer customer = cust_ArrayList.get (c);
            ArraySort<Account> accounts = customer.get_all_accounts ();

            // Show all of the accounts this customer has
            int a_size = accounts.size ();
            for (int a = 0; a < a_size; a++)
            {
                Account account = accounts.get (a);
                String account_status = account.get_account_type ();

                if (account_status != null && DISPLAY_ACTIVE == 1)
                // User choice 1 which is to show only active accounts
                {
                    System.out.print (customer.get_customer_name () + "\t");
                    System.out.print (customer.get_id () + "\t");
                    System.out.print (account.get_account_number () + "\t\t");
                    System.out.print (customer.get_pin () + "\t");
                    double balance = account.get_balance ();
                    NumberFormat dollar = NumberFormat.getCurrencyInstance ();
                    System.out.print (dollar.format (balance) + "\n");
                }
                else if (DISPLAY_ACTIVE == 2)
                // User choice 2 which is to show all accounts

                {
                    System.out.print (customer.get_customer_name () + "\t");
                    System.out.print (customer.get_id () + "\t");
                    System.out.print (account.get_all_account_number () + "\t\t");
                    System.out.print (customer.get_pin () + "\t");
                    double balance = account.get_balance ();
                    NumberFormat dollar = NumberFormat.getCurrencyInstance ();
                    System.out.print (dollar.format (balance) + "\t" + "\t");

                    if (account_status != null) System.out.print ("Active\n");
                    else System.out.print ("Closed\n");
                }
            }
        }
    }

    public void admin_show_accounts_by_balance (int DISPLAY_ACTIVE)
    {
        if (0 == cust_ArrayList.size ())
        {
            System.out.println ("\nWe don't have any customer yet.");
            return;
        }

        // Put all customer accounts into 1 list
        ArraySort<Account> all_accounts = new ArraySort<Account> (20);

        for (int c = 0, c_size = cust_ArrayList.size (); c < c_size; c++)
        {
            Customer customer = cust_ArrayList.get (c);
            ArraySort<Account> accounts = (ArraySort<Account>) customer.get_all_accounts ();
            all_accounts.addAll (accounts);
        }

        // Sort list of all accounts by highest to lowest balance
        all_accounts.sort (null);

        // Show all accounts
        System.out.println ("\n[Name]\t[ID]\t[Account#]\t[Pin#]\t[Balance]");

        for (int a = 0, a_size = all_accounts.size (); a < a_size; a++)
        {
            Account account = all_accounts.get (a);
            Customer customer = account.get_customer ();

            String account_status = account.get_account_type ();

            if (account_status != null && DISPLAY_ACTIVE == 1)
            // User choice 1 which is to show only active accounts
            {
                System.out.print (customer.get_customer_name () + "\t");
                System.out.print (customer.get_id () + "\t");
                System.out.print (account.get_account_number () + "\t\t");
                System.out.print (customer.get_pin () + "\t");
                double balance = account.get_balance ();
                NumberFormat dollar = NumberFormat.getCurrencyInstance ();
                System.out.print (dollar.format (balance) + "\n");

            }

            else if (DISPLAY_ACTIVE == 2)
            // User choice 2 which is to show all accounts
            {
                System.out.print (customer.get_customer_name () + "\t");
                System.out.print (customer.get_id () + "\t");
                System.out.print (account.get_all_account_number () + "\t\t");
                System.out.print (customer.get_pin () + "\t");
                double balance = account.get_balance ();
                NumberFormat dollar = NumberFormat.getCurrencyInstance ();
                System.out.print (dollar.format (balance) + "\t");

                if (account_status != null) System.out.print ("Active\n");
                else System.out.print ("Closed\n");

            }

        }
    }

    public void admin_show_customer_accounts ()
    {
        if (0 == cust_ArrayList.size ())
        {
            System.out.println ("\nWe don't have any customer yet.");
            return;
        }

        // Get a customer ID and customer object
        validate_id ();

        System.out.println ("\n[Name]\t[ID]\t[Account#]\t[Pin#]\t[Balance]");

        ArraySort<Account> accounts = customer.get_all_accounts ();
        int a_size = accounts.size ();

        for (int a = 0; a < a_size; a++)
        {
            Account account = accounts.get (a);

            // Show the details for each of the customer's accounts
            System.out.print (customer.get_customer_name () + "\t");
            System.out.print (customer.get_id () + "\t");
            System.out.print (account.get_account_number () + "\t\t");
            System.out.print (customer.get_pin () + "\t");
            double balance = account.get_balance ();
            NumberFormat dollar = NumberFormat.getCurrencyInstance ();
            System.out.print (dollar.format (balance) + "\n");
        }
    }

    @SuppressWarnings("unchecked")
    public void read_customer_info_from_file ()
    {
        try
        {
            File file = new File ("p1.dat");	// Get access to the p1.dat file
            file.createNewFile (); 				// Make sure it exists

            FileInputStream fs = new FileInputStream ("p1.dat");
            ObjectInputStream os = new ObjectInputStream (fs);

            cust_ArrayList =   (ArraySort<Customer>) os.readObject ();	// read
                                                                    	// all
                                                                    	// customers
                                                                    	// from
                                                                    	// file
            customer_count = os.readInt ();					// read customer
                                           					// count from file
            account_count = os.readInt ();					// read account
                                          					// count from file
            transaction_counter = os.readInt ();	// read transaction counter
                                                	// (for counting log-in)

            os.close ();
            fs.close ();
        }
        catch (EOFException e)
        {
            // Do nothing. This happens when the file is empty.
        }
        catch (Exception e)
        {
            System.out.println ("\nError reading p1.dat\t" + cust_ArrayList);
            e.printStackTrace ();
        }
    }

    public void write_customer_info_to_file ()
    {
        try
        {
            FileOutputStream fs = new FileOutputStream ("p1.dat");
            ObjectOutputStream os = new ObjectOutputStream (fs);

            os.writeObject (cust_ArrayList);			// write all customers
                                            			// to file
            os.writeInt (customer_count);	// write customer count to file
            os.writeInt (account_count);	// write account count to file
            os.writeInt (transaction_counter);	// write transaction counter
                                              	// (for counting log-in)



            os.close ();
            fs.close ();
        }
        catch (Exception e)
        {
            System.out.println ("\nError writing p1.dat");
            e.printStackTrace ();
        }
    }

} // Last braces (to close the class)

/*
 * Description: You will implement the constructor and methods for the following
 * class to support a simple online banking system. Continue to use
 * ConsoleReader.java code and add the code to hw2.java. For HW #2, you just
 * need to implement options 1, 2, 3, 6, and 9. Option 1,2,3 can share the Get
 * Balance at the end of each option processed.
 */

//
// Description: You will implement the constructor and methods for the
// following class to support a simple online banking system. Continue
// to use ConsoleReader.java code and add on top of homework #2 For
// P1, you will complete the remain options: 0, 4, 5, and 7. In
// addition, Here are a few additional requirements.
//
// -- Rename hw2.java to p1.java
// -- Existing customer information will be saved/retrieved in a file call
// "p1.dat" in the same directory.
// -- Handle all the appropriate exceptions such as bad input.
// -- The administrator function is now expanded to the following:
// -- After the correct hardcoded password "abcd" is entered, display the
// following choices.
// -- 1. Display all the accounts in alphabetical order based on the customer
// name.
// -- 2. Display all the accounts in order of highest balance to lowest balance.
// -- 3. Display all the accounts belonging to the same customer ID.
// -- New information should be saved to "p1.dat" whenever there are any changes
// to customer balance and/or accounts such as Open, Deposit, Withdraw,
// Transfer,
// and any internal interest updates.
// -- The new output format for each account should fit in 1 horizontal line.
// -- When a customer closes an account, that account will never be deleted.
// Instead it will be cleared and marked as inactive.
// -- Initially the data file will not be there, therefore you must handle the
// "FileNotFoundException" and continue.

/*
 * Welcome to the CS49J Banking System
 *
 * 1) Create Customer
 * 2) Open Account
 * 3) Deposit
 * 4) Withdraw
 * 5) Transfer
 * 6) Get Balance
 * 7) Close Account
 * 9) Exit
 *
 * Actions for each option chosen within the Atm Class:
 *
 * 1) Create customer --hw2
 * - Prompt for customer name and a 4 digits/characters PIN
 * - Generate a customer ID
 * - Add new customer to array list
 *
 * 2) Open account--hw2
 * - Get customer ID and pin to login.
 * - Validate user information.
 * - Generate an account number, add to acct array list
 * - Display account information.
 *
 * 3) Deposit--hw2
 * - Get customer ID and pin to login.
 * - Validate user information.
 * - Get the deposit amount.
 * - Update the balance.
 * - Display account information.
 *
 * 4) Withdraw
 * - Get customer ID and pin to login.
 * - Validate user information.
 * - Get the withdraw amount.
 * - Validate and update balance
 * - Display account information.
 *
 * 5) Transfer
 * - Get customer ID and pin to login.
 * - Validate user information.
 * - Get the transfer amount.
 * - Validate and update balance on both accounts.
 * - Display account information.
 *
 * 6) Get Balance--hw2
 * - Get customer ID and pin to login.
 * - Validate user information.
 * - Display account information.
 *
 * 7) Close account
 * - Get customer ID and pin to login.
 * - Validate user information.
 *
 *
 * Notes: PIN is a 4 digit character string. Both Customer ID and account # are
 * “system” generated. ID starts with 101 and Account # starts with 1001 (Hint:
 * add a public static attribute in both Customer and Account) You should not
 * make any changes to ConsoleReader.java file. You need to submit 5 files: 4
 * *.java files and hw2.readme. Print the appropriate error message when
 * necessary. Make sure there are plenty of comments in the code. You should
 * have a log-in method inside Atm to prompt for customer ID and pin. You should
 * have a validate_ID_PW method inside Atm to validate the ID and pin from the
 * customer array-list.
 */
