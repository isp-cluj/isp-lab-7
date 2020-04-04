package isp.lab7.safehome;

import isp.lab7.safehome.exceptions.InvalidPinException;
import isp.lab7.safehome.exceptions.TenantAlreadyExistsException;
import isp.lab7.safehome.exceptions.TenantNotFoundException;
import isp.lab7.safehome.exceptions.TooManyAttemptsException;

import java.time.LocalDateTime;
import java.util.*;

public class DoorLockController implements ControllerInterface {
    private static final String ENTER_PIN_OPERATION = "enterPin";
    private static final String ADD_TENANT_OPERATION = "addTenant";
    private static final Integer MAX_RETRIES = 3;
    private Door door;
    private Map<Tenant, AccessKey> validAccess;
    private List<AccessLog> accessLogs;
    private Integer currentRetries = 0;

    public DoorLockController() {
        this.door = new Door();
        this.validAccess = new HashMap<>();
        this.validAccess.put(new Tenant(ControllerInterface.MASTER_TENANT_NAME), new AccessKey(ControllerInterface.MASTER_KEY));
        this.accessLogs = new ArrayList<>();
    }

    @Override
    public DoorStatus enterPin(String pin) throws Exception {
        final Map.Entry<Tenant, AccessKey> accessKeyEntry = this.validAccess
                .entrySet()
                .stream()
                .filter(tenantAccessKeyEntry -> tenantAccessKeyEntry.getValue().getPin().equals(pin))
                .findFirst().orElse(null);

        if (Objects.isNull(accessKeyEntry)) { // invalid pin
            currentRetries ++;
            final AccessLog errorAccessLog = new AccessLog("-", LocalDateTime.now(), ENTER_PIN_OPERATION, door.getStatus(), "");

            if (currentRetries >= MAX_RETRIES) { // max attempts
                errorAccessLog.setErrorMessage("TooManyAttemptsException");
                this.currentRetries = 3;
                this.accessLogs.add(errorAccessLog);
                throw new TooManyAttemptsException();
            } else {
                errorAccessLog.setErrorMessage("InvalidPinException");
                this.accessLogs.add(errorAccessLog);
                throw new InvalidPinException();
            }
        } else { // valid pin
            if (accessKeyEntry.getKey().getName().equals(ControllerInterface.MASTER_KEY)) { // reset retries
                this.currentRetries = 0;
            }

            if (door.getStatus() == DoorStatus.OPEN) {
                door.lockDoor();
            } else {
                door.unlockDoor();
            }

            final AccessLog successAccessLog = new AccessLog("-", LocalDateTime.now(), ENTER_PIN_OPERATION, door.getStatus(), "");
            this.accessLogs.add(successAccessLog);
            return door.getStatus();
        }
    }

    @Override
    public void addTenant(String pin, String name) throws Exception {
        final Tenant tenant = new Tenant(name);
        final AccessLog accessLog = new AccessLog(name, LocalDateTime.now(), ADD_TENANT_OPERATION, door.getStatus(), "");

        if (this.validAccess.containsKey(tenant)) {
            accessLog.setErrorMessage("TenantAlreadyExistsException");
            this.accessLogs.add(accessLog);
            throw new TenantAlreadyExistsException();
        }

        this.accessLogs.add(accessLog);
        this.validAccess.put(tenant, new AccessKey(pin));
    }

    @Override
    public void removeTenant(String name) throws Exception {
        final Tenant tenant = new Tenant(name);
        final AccessLog accessLog = new AccessLog(name, LocalDateTime.now(), ADD_TENANT_OPERATION, door.getStatus(), "");

        if (!this.validAccess.containsKey(tenant)) {
            accessLog.setErrorMessage("TenantNotFoundException");
            this.accessLogs.add(accessLog);
            throw new TenantNotFoundException();
        }

        this.accessLogs.add(accessLog);
        this.validAccess.remove(tenant);
    }

    public List<AccessLog> getAccessLogs() {
        return accessLogs;
    }
}
