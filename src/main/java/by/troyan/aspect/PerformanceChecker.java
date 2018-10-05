package by.troyan.aspect;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

/**
 * The class holds method check performance. That allow to find how much time any method
 * special annotation with will work.
 */
@Aspect
@Component
public class PerformanceChecker {
    private static final Logger LOG = LogManager.getLogger(PerformanceChecker.class);

    /**
     * This method uses proxy to wrap all methods that have @TimeCounted annotation
     * It gets time before method, run method, and gets time again. Information will
     * withdraw in logs.
     *
     * @param proceedingJoinPoint the proceeding join point - according to requirements
     * @return the object - according to requirements
     */
    @Around("@annotation(by.troyan.aspect.TimeCounted)")
    public Object checkPerformance(ProceedingJoinPoint proceedingJoinPoint) {
        Object value = null;
        try {
            long start = System.nanoTime();
            value = proceedingJoinPoint.proceed();
            long end = System.nanoTime();
            LOG.info("The performance of [" + proceedingJoinPoint.getSignature().getName() +
                    "] method is: " + (end - start) + " nanoseconds.");
            return value;
        } catch (Throwable e) {
            LOG.error("Exception: " + e);
        }
        return value;
    }
}
