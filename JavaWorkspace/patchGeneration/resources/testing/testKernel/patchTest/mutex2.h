#ifndef __LINUX_MUTEX_H
#define __LINUX_MUTEX_H

#include <someFile.h>

#define noRemoveSingleLineMacro(lock) macroBody(lock)
#define noRemoveMultiLineMacro(lock)	\
		do { 							\
			lock->doSomething();		\
		} while (0);

static inline void noRemoveSingleLineFunctionPrototype(struct mutex *lock);
static inline
	void noRemoveMultiLineFunctionPrototype(struct mutex *lock)
	__acquires(lock);
static inline int noRemoveMultiLineFunctionImplementation(struct mutex *lock)
{
	return lock->doSomething();
}

/*
 * This is a comment.
 */
#define mutexSingleLineMacroToRemove(lock) macroBody(lock)
#define mutexMultiLineMacroToRemove(lock) 	\
		do {								\
			lock->doSomething();			\
		} while (0);

static inline void single_line_mutex_lock_function_to_remove(struct mutex *lock);
static inline
	void multi_line_mutex_lock_function_to_remove(struct mutex *lock)
	__acquires(lock);
static inline int mutex_lock_function_implementation_to_remove(struct mutex *lock)
{
	return lock->doSomething();
}
EXPORT_SYMBOL(mutex_lock_function_implementation_to_remove);

static inline struct patchTest *function(void *lock)
{
	return NULL;
}

#define macro(lock) function(lock)

#endif /* __LINUX_MUTEX_H */
