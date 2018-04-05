/**************************************************************/
// SPIN LOCK
/**************************************************************/

#ifndef __LINUX_L_SAP_SPINLOCK_H
#define __LINUX_L_SAP_SPINLOCK_H
#include <linux/spinlock_types.h>

static inline void __raw_spin_lock(void *lock){}
static inline int __raw_spin_trylock(void *lock){return 0;}
static inline void __raw_spin_unlock(void *lock){}

// Define a macro wrapper for extra functions for query unification.
#define # define raw_spin_lock_nest_lock(lock, nest_lock) __raw_spin_lock(lock)
#define # define raw_spin_lock_nested(lock, subclass) __raw_spin_lock(lock)
#define _raw_spin_lock(lock) __raw_spin_lock(lock)
#define _raw_spin_lock_bh(lock) __raw_spin_lock(lock)
#define _raw_spin_lock_irq(lock) __raw_spin_lock(lock)
#define _raw_spin_lock_irqsave(lock) __raw_spin_lock(lock)
#define _raw_spin_trylock(lock) __raw_spin_trylock(lock)
#define _raw_spin_trylock_bh(lock) __raw_spin_trylock(lock)
#define _raw_spin_unlock(lock) __raw_spin_unlock(lock)
#define atomic_dec_and_lock(atomic, lock) __raw_spin_lock(lock)
#define do_raw_spin_lock_flags(lock, flags) __raw_spin_lock(lock)
#define raw_spin_lock_bh(lock) __raw_spin_lock(lock)
#define raw_spin_lock_irq(lock) __raw_spin_lock(lock)
#define raw_spin_lock_irqsave(lock, flags) __raw_spin_lock(lock)
#define raw_spin_lock_irqsave_nested(lock, flags, subclass) __raw_spin_lock(lock)
#define raw_spin_trylock_bh(lock) __raw_spin_trylock(lock)
#define raw_spin_trylock_irq(lock) __raw_spin_trylock(lock)
#define raw_spin_trylock_irqsave(lock, flags) __raw_spin_trylock(lock)
#define raw_spin_unlock(lock) __raw_spin_unlock(lock)
#define raw_spin_unlock_bh(lock) __raw_spin_unlock(lock)
#define raw_spin_unlock_irq(lock) __raw_spin_unlock(lock)
#define raw_spin_unlock_irqrestore(lock, flags) __raw_spin_unlock(lock)
#define spin_lock_irqsave(lock, flags) __raw_spin_lock(lock)
#define spin_lock_irqsave_nested(lock, flags, subclass) __raw_spin_lock(lock)
#define spin_lock_nest_lock(lock, nest_lock) __raw_spin_lock(lock)
#define spin_lock_nested(lock, subclass) __raw_spin_lock(lock)
#define spin_trylock_irqsave(lock, flags) __raw_spin_trylock(lock)

#endif /* __LINUX_L_SAP_SPINLOCK_H */

