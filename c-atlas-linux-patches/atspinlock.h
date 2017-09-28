#ifndef __LINUX_ATSPINLOCK_H
#define __LINUX_ATSPINLOCK_H
#include <linux/spinlock_types.h>


/**************************************************************/
// SPIN LOCK
/**************************************************************/
static inline void __raw_spin_lock(spinlock_t *lock){}
static inline int __raw_spin_trylock(spinlock_t *lock){return 0;}
static inline void __raw_spin_unlock(spinlock_t *lock){}
//////////////// spin_lock
# define spin_lock(lock)			__raw_spin_lock(lock)
# define spin_lock_bh(lock)		__raw_spin_lock(lock)
# define spin_lock_irq(lock)		__raw_spin_lock(lock)
# define spin_lock_irqsave(lock, flags)	__raw_spin_lock(lock)
# define spin_lock_irqsave_nested(lock, flags, subclass)			__raw_spin_lock(lock)
# define spin_lock_nested(lock, subclass) __raw_spin_lock(lock)
# define spin_lock_nest_lock(lock, nest_lock) __raw_spin_lock(lock)

//////////////// _spin_lock
# define _spin_lock(lock)			__raw_spin_lock(lock)
# define _spin_lock_bh(lock)		__raw_spin_lock(lock)
# define _spin_lock_irq(lock)		__raw_spin_lock(lock)
# define _spin_lock_irqsave(lock, flags)	__raw_spin_lock(lock)
# define _spin_lock_irqsave_nested(lock, flags, subclass)			__raw_spin_lock(lock)
# define _spin_lock_nested(lock, subclass) __raw_spin_lock(lock)
# define _spin_lock_nest_lock(lock, nest_lock) __raw_spin_lock(lock)

//////////////// raw_spin_lock
# define raw_spin_lock(lock)		__raw_spin_lock(lock)
# define raw_spin_lock_flags(lock, flags) __raw_spin_lock(lock)
# define raw_spin_lock_nested(lock, subclass)		__raw_spin_lock(lock)
# define raw_spin_lock_nest_lock(lock, nest_lock)	__raw_spin_lock(lock)
# define raw_spin_lock_irqsave(lock, nest_lock)	__raw_spin_lock(lock)
# define raw_spin_lock_irqsave_nested(lock, flags, subclass)	__raw_spin_lock(lock)
# define raw_spin_lock_irq(lock)		__raw_spin_lock(lock)
# define raw_spin_lock_bh(lock)		__raw_spin_lock(lock)

//////////////// _raw_spin_lock
# define _raw_spin_lock(lock)		__raw_spin_lock(lock)
# define _raw_spin_lock_flags(lock, flags) __raw_spin_lock(lock)
# define _raw_spin_lock_nested(lock, subclass)		__raw_spin_lock(lock)
# define _raw_spin_lock_nest_lock(lock, nest_lock)	__raw_spin_lock(lock)
# define _raw_spin_lock_irqsave(lock, nest_lock)	__raw_spin_lock(lock)
# define _raw_spin_lock_irqsave_nested(lock, flags, subclass)	__raw_spin_lock(lock)
# define _raw_spin_lock_irq(lock)		__raw_spin_lock(lock)
# define _raw_spin_lock_bh(lock)		__raw_spin_lock(lock)

//////////////// do_raw_spin_lock
# define do_raw_spin_lock(lock)			__raw_spin_lock(lock)
# define do_raw_spin_lock_flags(lock, flags)			__raw_spin_lock(lock)

//////////////// spin_trylock
# define spin_trylock(lock)		__raw_spin_trylock(lock)
# define spin_trylock_bh(lock)	__raw_spin_trylock(lock)
# define spin_trylock_irq(lock)   __raw_spin_trylock(lock)
# define spin_trylock_irqsave(lock, flags)  __raw_spin_trylock(lock)
# define atomic_dec_and_lock(atomic, lock) __raw_spin_trylock(lock)

//////////////// _spin_trylock
# define _spin_trylock(lock)		__raw_spin_trylock(lock)
# define _spin_trylock_bh(lock)	__raw_spin_trylock(lock)
# define _spin_trylock_irq(lock)   __raw_spin_trylock(lock)
# define _spin_trylock_irqsave(lock, flags)  __raw_spin_trylock(lock)

//////////////// raw_spin_trylock
# define do_raw_spin_trylock(lock)		__raw_spin_trylock(lock)
# define raw_spin_trylock(lock)		__raw_spin_trylock(lock)
# define raw_spin_trylock_bh(lock)		__raw_spin_trylock(lock)
# define raw_spin_trylock_irq(lock)		__raw_spin_trylock(lock)
# define raw_spin_trylock_irqsave(lock, flags)		__raw_spin_trylock(lock)
# define _raw_spin_trylock(lock)	__raw_spin_trylock(lock)

# define _atomic_dec_and_lock(atomic, lock) __raw_spin_trylock(lock)

 //////////////// spin_unlock
# define spin_unlock(lock)		__raw_spin_unlock(lock)
# define spin_unlock_bh(lock)		__raw_spin_unlock(lock)
# define spin_unlock_irq(lock)		__raw_spin_unlock(lock)
# define spin_unlock_irqrestore(lock, flags)		__raw_spin_unlock(lock)
# define spin_unlock_wait(lock)	__raw_spin_unlock(lock)

# define _spin_unlock(lock)		__raw_spin_unlock(lock)
# define _spin_unlock_bh(lock)		__raw_spin_unlock(lock)
# define _spin_unlock_irq(lock)		__raw_spin_unlock(lock)
# define _spin_unlock_irqrestore(lock, flags)		__raw_spin_unlock(lock)
# define _spin_unlock_wait(lock)	__raw_spin_unlock(lock)

# define raw_spin_unlock(lock)		__raw_spin_unlock(lock)
# define raw_spin_unlock_irq(lock)	__raw_spin_unlock(lock)
# define raw_spin_unlock_irqrestore(lock, flags)		__raw_spin_unlock(lock)
# define raw_spin_unlock_bh(lock)	__raw_spin_unlock(lock)
# define raw_spin_unlock_wait(lock)	__raw_spin_unlock(lock)

# define _raw_spin_unlock(lock)		__raw_spin_unlock(lock)

# define do_raw_spin_unlock(lock)	__raw_spin_unlock(lock)


#endif /* __LINUX_ATSPINLOCK_H */
