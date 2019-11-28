package org.mtokarski.object.header.age;

import org.mtokarski.object.header.HeaderUtil;

import javax.management.Notification;
import javax.management.NotificationEmitter;
import java.lang.management.GarbageCollectorMXBean;
import java.lang.management.ManagementFactory;
import java.util.List;
import java.util.function.LongPredicate;

public class AgeUtil {

    public static void printAgeIncrease(Object object) {
        printAgeIncrease(object, i -> true);
    }

    public static void printAgeIncrease(Object object, LongPredicate ageChanged) {
        long age;
        long lastAge = -1;
        boolean ifContinue = true;
        do {
            double[] mockTab = new double[200_000];
            age = HeaderUtil.getObjectAge(object);
            if (age != lastAge) {
                lastAge = age;
                System.out.println("New minor gc, current object age: " + age);
                ifContinue = ageChanged.test(age);
            }
        } while (ifContinue && !Thread.currentThread().isInterrupted());
    }

    public static void registerListener() {
        List<GarbageCollectorMXBean> gcBeans = ManagementFactory.getGarbageCollectorMXBeans();
        for (GarbageCollectorMXBean gcBean : gcBeans) {
            NotificationEmitter notificationEmitter = (NotificationEmitter) gcBean;
            notificationEmitter.addNotificationListener(AgeUtil::printNotificationInfo, null, null);
        }
    }

    public static void printNotificationInfo(Notification notification, Object o) {

        System.out.println(notification.getTimeStamp() + " New event occured: " + notification.getMessage());
    }
}
