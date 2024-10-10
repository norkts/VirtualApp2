package android.os;

import androidx.annotation.NonNull;
import android.util.AndroidException;

/**
 * Parent exception for all Binder remote-invocation errors
 */
public class RemoteException extends AndroidException {
    public RemoteException() {
        super();
    }

    public RemoteException(String message) {
        super(message);
    }

    /** @hide */
    public RemoteException(String message, Throwable cause, boolean enableSuppression,
                           boolean writableStackTrace) {
        super();
    }

    /** @hide */
    public RemoteException(Throwable cause) {
        this(cause.getMessage(), cause, true, false);
    }

    /**
     * Rethrow this as an unchecked runtime exception.
     * <p>
     * Apps making calls into other processes may end up persisting internal
     * state or making security decisions based on the perceived success or
     * failure of a call, or any default values returned. For this reason, we
     * want to strongly throw when there was trouble with the transaction.
     *
     * @throws RuntimeException
     */
    @NonNull
    public RuntimeException rethrowAsRuntimeException() {
        throw new RuntimeException(this);
    }

    /**
     * Rethrow this exception when we know it came from the system server. This
     * gives us an opportunity to throw a nice clean
     * {@link DeadSystemRuntimeException} signal to avoid spamming logs with
     * misleading stack traces.
     * <p>
     * Apps making calls into the system server may end up persisting internal
     * state or making security decisions based on the perceived success or
     * failure of a call, or any default values returned. For this reason, we
     * want to strongly throw when there was trouble with the transaction.
     *
     * @throws RuntimeException
     */
    @NonNull
    public RuntimeException rethrowFromSystemServer() {
        if (this instanceof DeadObjectException) {
            throw new DeadSystemRuntimeException();
        } else {
            throw new RuntimeException(this);
        }
    }
}
