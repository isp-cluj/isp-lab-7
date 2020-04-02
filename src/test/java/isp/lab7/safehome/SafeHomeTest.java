package isp.lab7.safehome;

import org.easymock.Mock;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class SafeHomeTest {

    ControllerInterface ctrl;

    @Before
    public void instantiateDoorController(){
        //INITIALISE CONTROLLER OBJECT HERE
    }

    // >>>> DO NOT CHANGE CODE AFTER THIS LINE ! <<<<

    @Test
    public void whenCorrectPinDoorOpen() throws Exception {
        ctrl.addTenant("12345", "Utilizator1");
        assertEquals(DoorStatus.OPEN, ctrl.enterPin("12345"));
    }

    @Test
    public void whenWrongPinThrowInvalidPinException(){
        ctrl.addTenant("1234", "Utilizator1");
        try {
            ctrl.enterPin("4532");
        }catch(Exception e){
            assertTrue(e.getClass().getCanonicalName().indexOf("InvalidPinException")!=-1);
        }
    }

    @Test
    public void whenTooManyAttemptsThrowTooManyAttemptsException(){
        ctrl.addTenant("1234", "Utilizator1");
        for(int i=0;i<4;i++){
            try{
                ctrl.enterPin("4532");
            }catch(Exception e){
                System.out.println("Attempt "+i);
                assertTrue("Wrong exception name.",e.getClass().getCanonicalName().indexOf("InvalidPinException")!=-1);
            }
        }//.for

        try {
            ctrl.enterPin("4532");
        }catch(Exception e){
            assertTrue("Wrong exception name.", e.getClass().getCanonicalName().indexOf("TooManyAttemptsException")!=-1);
        }
    }

    @Test
    public void whenEnterPinDoorRevertStatus() throws Exception {
        ctrl.addTenant("12345", "Utilizator1");
        assertEquals(DoorStatus.OPEN, ctrl.enterPin("12345"));
        assertEquals(DoorStatus.CLOSE, ctrl.enterPin("12345"));
        assertEquals(DoorStatus.OPEN, ctrl.enterPin("12345"));
        assertEquals(DoorStatus.CLOSE, ctrl.enterPin("12345"));
    }

    @Test
    public void whenTooManyAttemptsDoorPermanetlyLocked(){
        ctrl.addTenant("1234", "Utilizator1");
        for(int i=0;i<4;i++){
            try{
                ctrl.enterPin("4532");
            }catch(Exception e){
                System.out.println("Attempt "+i);
                assertTrue("Wrong exception name.",e.getClass().getCanonicalName().indexOf("InvalidPinException")!=-1);
            }
        }//.for


        int errCount = 0;
        for(int i=0;i<4;i++) {
            try {
                ctrl.enterPin("4532");
            } catch (Exception e) {
                assertTrue("Wrong exception name.", e.getClass().getCanonicalName().indexOf("TooManyAttemptsException") != -1);
                errCount++;
            }
        }

        assertEquals("Door is not permanently locked after too many attempts.", 4, errCount);

    }

    @Test
    public void whenTenantRemovedAccessIsALsoRemoved() throws Exception{
        ctrl.addTenant("12345", "Utilizator1");
        assertEquals(DoorStatus.OPEN, ctrl.enterPin("12345"));
        ctrl.removeTenant("Utilizator1");
        try {
            ctrl.enterPin("12345");
        } catch (Exception e) {
            assertTrue("Wrong exception name.", e.getClass().getCanonicalName().indexOf("InvalidPinException") != -1);
        }

    }
}
