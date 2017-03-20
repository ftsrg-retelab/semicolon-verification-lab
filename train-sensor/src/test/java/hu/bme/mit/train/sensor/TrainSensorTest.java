package hu.bme.mit.train.sensor;

import hu.bme.mit.train.controller.TrainControllerImpl;
import hu.bme.mit.train.interfaces.TrainController;
import hu.bme.mit.train.interfaces.TrainSensor;
import hu.bme.mit.train.interfaces.TrainUser;
import hu.bme.mit.train.user.TrainUserImpl;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import static org.mockito.Mockito.*;

public class TrainSensorTest {

    TrainSensor sensor;
    TrainController controller;
    TrainUser user;

    ///

    @Before
    public void before() {
        controller = mock(TrainControllerImpl.class);
        user = mock(TrainUserImpl.class);
        sensor = new TrainSensorImpl(controller, user);
    }

    @Test
    public void defaultStateTest() {
        Assert.assertEquals(false, user.getAlarmState());
    }

    @Test
    public void userAlarmTest() {
        when(controller.getReferenceSpeed()).thenReturn(10);

        sensor.overrideSpeedLimit(-2);

        sensor.overrideSpeedLimit(2);

        sensor.overrideSpeedLimit(6548);
        verify(user, times(3)).setAlarmState(true);
    }

    @Test
    public void userSetAlarmBackToFalseTest(){
        when(user.getAlarmState()).thenReturn(true);
        sensor.overrideSpeedLimit(6);
        verify(user, times(1)).setAlarmState(false);
    }

    @Test
    public void speedLimitFunctionNotCalledTest() {
        when(controller.getReferenceSpeed()).thenReturn(10);
        sensor.overrideSpeedLimit(2);
        verify(controller, times(0)).setSpeedLimit(2);
    }

}
