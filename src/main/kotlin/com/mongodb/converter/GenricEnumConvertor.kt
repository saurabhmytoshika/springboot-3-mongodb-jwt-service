//package com.mongodb.entity.support
//
//class GenericEnumConverter {
//
//    companion object {
//
//        //Int to Enum
//        inline fun < reified T : Enum<T>> Int.convertToEnum(): Enum<T>? {
//            return enumValues<T>().firstOrNull { it.ordinal == this }
//
//        }
//
//        //Enum to Int
//        inline fun< T : Enum<T>> T.convertToInt(): Int {
//            return this.ordinal
//        }
//    }
//}