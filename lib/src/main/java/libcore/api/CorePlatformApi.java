package libcore.api;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.TYPE, ElementType.FIELD, ElementType.METHOD, ElementType.CONSTRUCTOR, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.SOURCE)
public @interface CorePlatformApi {
   Status status() default CorePlatformApi.Status.LEGACY_ONLY;

   public static enum Status {
      STABLE,
      LEGACY_ONLY;
   }
}
