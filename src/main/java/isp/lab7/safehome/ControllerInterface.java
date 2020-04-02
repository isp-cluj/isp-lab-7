package isp.lab7.safehome;

public interface ControllerInterface {
    public DoorStatus enterPin(String pin) throws Exception;
    public void addTenant(String pin, String name);
    public void removeTenant(String name);
}
