package messages;

public class PackageToAnotherBank
{
    private int fromBank;
    private int fromAccount;
    private int toBank;
    private int toAccount;
    private double value;
    private TypeOfPackage typeOfPackage;

    public int getFromBank()
    {
        return fromBank;
    }

    public int getFromAccount()
    {
        return fromAccount;
    }

    public int getToBank()
    {
        return toBank;
    }

    public int getToAccount()
    {
        return toAccount;
    }

    public double getValue()
    {
        return value;
    }

    public TypeOfPackage getTypeOfPackage()
    {
        return typeOfPackage;
    }

    public PackageToAnotherBank(int fromBank, int fromAccount, int toBank, int toAccount, double value, TypeOfPackage typeOfPackage)
    {
        this.fromBank = fromBank;
        this.fromAccount = fromAccount;
        this.toBank = toBank;
        this.toAccount = toAccount;
        this.value = value;
        this.typeOfPackage = typeOfPackage;
    }
}