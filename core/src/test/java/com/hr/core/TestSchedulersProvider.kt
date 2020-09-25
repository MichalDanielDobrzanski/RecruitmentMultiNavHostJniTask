package com.hr.core

import com.hr.core.schedulers.SchedulersProvider
import io.reactivex.schedulers.Schedulers

class TestSchedulersProvider : SchedulersProvider {
    override fun background() = Schedulers.trampoline()
    override fun io() = Schedulers.trampoline()
    override fun ui() = Schedulers.trampoline()
}