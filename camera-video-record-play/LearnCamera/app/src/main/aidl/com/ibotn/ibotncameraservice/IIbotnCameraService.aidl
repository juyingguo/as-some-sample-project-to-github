// IIbotnCameraService.aidl
package com.ibotn.ibotncameraservice;
import com.ibotn.ibotncameraservice.IReleaseCamera;

// Declare any non-default types here with import statements

interface IIbotnCameraService {
    /**
     * Demonstrates some basic types that you can use as parameters
     * and return values in AIDL.
     */
//    void basicTypes(int anInt, long aLong, boolean aBoolean, float aFloat,
//            double aDouble, String aString);
    boolean canUseCamera(IReleaseCamera iReleaseCamera,int yourLevel);
    int getLevel();
    void setLevel(int level);
}
