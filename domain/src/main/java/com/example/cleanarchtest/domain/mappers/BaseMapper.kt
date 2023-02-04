package com.example.cleanarchtest.domain.mappers
// можно убрать in/out и проверить чо будет
interface BaseMapper<in A, out B> {

    fun map(type: A?): B
}