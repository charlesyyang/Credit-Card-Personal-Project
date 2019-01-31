import java.util.TreeMap;
import java.util.Scanner;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

/** @author Charles Yang */
public class Provider {
	
	public static void main(String[] args) {
		String textFile;

        if (args.length == 0) {
            Scanner input = new Scanner(System.in);
		    textFile = input.nextLine();
            input.close();
        } else {
            textFile = args[0];
        }
		
		BufferedReader reader = null;

		try {
			reader =  new BufferedReader(new FileReader(textFile));
			String currentLine = reader.readLine();
			
			while (currentLine != null) {
				String[] info = currentLine.split(" ");
				if (info[0].equals("Add")) {
					if (Check(info[2])){
						add(info[1], info[2], dolla(info[3]), true);
					}
					else {
						add(info[1], info[2], dolla(info[3]), false);
					}	
				}
				else if (info[0].equals("Charge")) {
					charge(info[1], dolla(info[2]));
				}
				else if (info[0].equals("Credit")) {
					credit(info[1], dolla(info[2]));
				}
			
				currentLine = reader.readLine();
        	}
		} catch(IOException e) {
			e.printStackTrace();
		} finally{
			try {
				reader.close();
			} catch(IOException ee) {
				ee.printStackTrace();
			}
		}

		/** Output */
		for (CreditCardAcc s: map.values()) {
			if (s.isValid()) {
				System.out.println(s.name + ":" + " " + "$" + s.balance);
			}
			else {
				System.out.println(s.name + ":" + " " + "error");
			}
		}
	}

	/** Method to remove the "$" and obtain the integer value from the string input. */
	private static int dolla(String moneyNumber) {
		moneyNumber = moneyNumber.replace("$", "");
		return Integer.parseInt(moneyNumber);
	}

	/** Method to add a new account that maps the user's name to their account. */
    private static void add(String name, String accountNumber, int limit, boolean validated) {
		CreditCardAcc acc = new CreditCardAcc(name, accountNumber, limit, validated);
		map.put(name,acc);
	}
	
	/** Method to charge an amount to a user's account. Invalid if the account is invalid
	 * or if the charge would raise the balance over the limit.
	 */
	private static void charge(String name, int chargeValue) {
		map.get(name).chargeAcc(chargeValue);
		
    }
    
	/** Method to lower the balance of a user's account. Invalid if the account is invalid
	 * or if the credit would lower the balance below the limit.
	 */
	private static void credit(String name, int creditValue) {
		map.get(name).creditAcc(creditValue);
    }
    
    /** Method Luhn 10 Check. Method was taken from an online github code. 
	 * https://github.com/eix128/gnuc-credit-card-checker/blob/master/CCCheckerPro/src/com/gnuc/java/ccc/Luhn.java 
	*/
	private static boolean Check(String ccNumber) {
		int sum = 0;
		boolean alternate = false;
		for (int i = ccNumber.length() - 1; i >= 0; i--) {
			int n = Integer.parseInt(ccNumber.substring(i, i + 1));
			if (alternate) {
				n *= 2;
				if (n > 9) {
					n = (n % 10) + 1;
				}
			}
			sum += n;
			alternate = !alternate;
		}
		return (sum % 10 == 0);
	}


	/** Maps owner's name to their account alphabetically */
	private static TreeMap<String, CreditCardAcc> map = new TreeMap<String, CreditCardAcc>();
	
}
/** Credit Card class keeping track of user accounts. */
class CreditCardAcc {

    public CreditCardAcc(String name, String accountNumber, int limit, boolean validity) {
        this.name = name;
        this.accountNumber = accountNumber;
        balance = 0;
		creditlimit = limit;
		valid = validity;
        
	}
	/** Charges to the account. */
    public void chargeAcc(int money) {
        if (balance + money <= creditlimit) {
            balance = balance + money;
        }
    }

	/** Credits to the account. */
    public void creditAcc(int money) {
        balance = balance - money;
    }

	/** Returns the validity of the account based on Luhn */
    public boolean isValid() {
        return valid;
    }


    /** Owner of the credit card */
    public String name;
    /** Current balance in the user's account. */
    public int balance;
    /** Current credit limit remaining for user's account. */
    public int creditlimit;
    /** Boolean value determining the validity of the account. (Luhn 10 check) */
    private boolean valid;
    /** Stores the credit account number. */
    public String accountNumber;
}


