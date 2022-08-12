package mx.erick.demograylog

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;
 
@Aspect
@Component
public class LoggingAspect {   
    private static final Logger LOGGER = LogManager.getLogger(LoggingAspect.class);
 
    //AOP expression for which methods shall be intercepted
    @Around("execution(* mx.erick..*(..)))")
    public Object profileAllMethods(ProceedingJoinPoint proceedingJoinPoint) throws Throwable 
    {
        MethodSignature methodSignature = (MethodSignature) proceedingJoinPoint.getSignature();
         
        //Get intercepted method details
        String className = methodSignature.getDeclaringType().getSimpleName();
        String methodName = methodSignature.getName();
         
        final StopWatch stopWatch = new StopWatch();
         
        //Measure method execution time
        stopWatch.start();
        Object result = proceedingJoinPoint.proceed();
        stopWatch.stop();
 
        //Log method execution time
        LOGGER.info("Execution time of " + className + "." + methodName + " :: " + stopWatch.getTotalTimeMillis() + " ms");       
        return result;
    }
    
    @Before("execution(* mx.erick..*(..)))")
    public void before(JoinPoint joinPoint) throws Throwable  {
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        String[] pNames = methodSignature.getParameterNames();
        String className = methodSignature.getDeclaringType().getSimpleName();
        String methodName = methodSignature.getName();
        Object[] lArgs = joinPoint.getArgs();
        String end ="";
        int i =0;
        for (Object object : lArgs) {
        	if (i!=0) {
				end += ", ";
			}
			end += pNames[i]+": "+object.toString();
			i++;
		}        
        LOGGER.info(className + "." + methodName + " << " + end);       

    }
    

    @SuppressWarnings("unchecked")
	@AfterReturning(
    		pointcut="execution(* mx.erick..*(..)))",
    		returning="objectReturn")
    public void after(JoinPoint joinPoint, Object objectReturn) throws Throwable  {
 
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        String className = methodSignature.getDeclaringType().getSimpleName();
        String methodName = methodSignature.getName();   
        LOGGER.info(className + "." + methodName + " >> " + objectReturn );       

    }
    
    
    @AfterThrowing(
    	    pointcut="execution(* mx.erick..*(..)))",
    	    throwing="exec")
    public void catchAllSQLSyntaxErrors(Exception exec) throws Throwable {
    	LOGGER.info(exec.getClass() + " XX " + exec.getMessage() );
    }
    
}