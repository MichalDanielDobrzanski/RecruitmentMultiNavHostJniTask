package com.hr.core.schedulers

import io.reactivex.Scheduler

interface SchedulersProvider {
    fun background(): Scheduler
    fun io(): Scheduler
    fun ui(): Scheduler
}