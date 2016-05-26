/**                                               _    __ ____
 *   _ __  ___ _____   ___   __  __   ___ __     / |  / /  __/
 *  |  _ \/ _ |  _  | / _ | / / / /  / __/ /    /  | / / /__
 *  |  __/ __ |  ___|/ __ |/ /_/ /__/ __/ /__  / / v  / /__
 *  |_| /_/ |_|_|\_\/_/ |_/____/___/___/____/ /_/  /_/____/
 *
 */

package org.parallelme.userlibrary.parallel;

import org.parallelme.userlibrary.Iterable;
import org.parallelme.userlibrary.datatypes.UserData;

/**
 * Base class for all parallel iterables.
 *
 * @author Wilson de Carvalho
 */
public interface ParallelIterable<E extends UserData<?>> extends Iterable<E> {
}
