package com.example.challengergg.entity.query

interface CountAndWinsTable<T> {
    fun getValue(): T;
    fun getCount(): Int;
    fun getWins(): Int;
}