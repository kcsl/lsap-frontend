#ifndef __LINUX_ATMUTEXLOCK_H
#define __LINUX_ATMUTEXLOCK_H
#include <linux/spinlock_types.h>


/**************************************************************/
// MUTEX LOCK
/**************************************************************/
static inline void mutex_lock_nested(struct mutex *lock, unsigned int subclass){}
static inline void _mutex_lock_nest_lock(struct mutex *lock, struct lockdep_map *nest){}
static inline int mutex_lock_killable_nested(struct mutex *lock, unsigned int subclass){subclass = 0; return 0;}
static inline int mutex_lock_interruptible_nested(struct mutex *lock, unsigned int subclass){subclass = 0; return 0;}
static inline int mutex_trylock(struct mutex *lock){return 0;}
static inline int atomic_dec_and_mutex_lock(atomic_t *cnt, struct mutex *lock){return 0;}
static inline void mutex_unlock(struct mutex *lock){}

////////////////
#define mutex_lock(lock) mutex_lock_nested(lock, 0)
#define mutex_lock_interruptible(lock) mutex_lock_interruptible_nested(lock, 0)
#define mutex_lock_killable(lock) mutex_lock_killable_nested(lock, 0)
#define mutex_lock_nest_lock(lock, nest_lock) _mutex_lock_nest_lock(lock, &(nest_lock)->dep_map)


#endif /* __LINUX_ATMUTEXLOCK_H */
