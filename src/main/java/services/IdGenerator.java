package services;

public class IdGenerator {
    private static int serviceId = 0;

    public static int generateServiceId() {
        serviceId++;
        return serviceId;
    }
}
