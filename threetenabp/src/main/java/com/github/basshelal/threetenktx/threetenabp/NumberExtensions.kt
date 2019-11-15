package com.github.basshelal.threetenktx.threetenabp

@PublishedApi
internal inline val Number.I: Int
    get() = this.toInt()

@PublishedApi
internal inline val Number.D: Double
    get() = this.toDouble()

@PublishedApi
internal inline val Number.F: Float
    get() = this.toFloat()

@PublishedApi
internal inline val Number.L: Long
    get() = this.toLong()