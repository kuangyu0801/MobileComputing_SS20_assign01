// ISensorReaderService.aidl
package mcteam08.assign.task02;

// Declare any non-default types here with import statements

interface ISensorReaderService {
    /**
     * Demonstrates some basic types that you can use as parameters
     * and return values in AIDL.
     */
    float[] getSensorAccelerometer();
    double[] getLocation();
}
