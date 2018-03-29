#ifndef __LINUX_L_SAP_MUTEX_H
#define __LINUX_L_SAP_MUTEX_H
#include <linux/spinlock_types.h>

// Definition for "mutex" related functions with empty bodies.
static inline void mutex_lock_nested(struct mutex *lock, unsigned int subclass){}
static inline void _mutex_lock_nest_lock(struct mutex *lock, struct lockdep_map *nest_lock){}
static inline int mutex_lock_interruptible_nested(struct mutex *lock, unsigned int subclass){return 0;}
static inline int mutex_lock_killable_nested(struct mutex *lock, unsigned int subclass){return 0;}
static inline int mutex_trylock(struct mutex *lock){return 0;}
static inline void mutex_unlock(struct mutex *lock){}
static inline int atomic_dec_and_mutex_lock(atomic_t *cnt, struct mutex *lock){return 0;}

// Define a macro wrapper for extra functions for query unification.
#define mutex_lock(lock)
#define mutex_lock_interruptible(lock)
#define mutex_lock_killable(lock)
#define mutex_lock_io(lock)
#define mutex_lock_nest_lock(lock, nest_lock)

#endif /* __LINUX_L_SAP_MUTEX_H */
