package com.example.hw16.utils

class IndexedList<T>(collection: Collection<T>, private val indexList: List<Int>? = null) :
    ArrayList<T>(collection) {
    override val size: Int
        get() = indexList?.size ?: super.size

    private fun getIndex(index: Int) = indexList?.get(index) ?: index

    override fun get(index: Int): T {
        return super.get(getIndex(index))
    }

    override fun isEmpty(): Boolean {
        return indexList?.isEmpty() ?: super.isEmpty()
    }

    override fun set(index: Int, element: T): T {
        return super.set(getIndex(index), element)
    }

    override fun removeAt(index: Int): T {
        return super.removeAt(getIndex(index))
    }

    override fun toString(): String {
        return "list size: (" + super.size.toString() + ", " + this.size + ")\n" + indexList.toString()
    }

    override fun subList(fromIndex: Int, toIndex: Int): MutableList<T> {
        return super.subList(getIndex(fromIndex), getIndex(toIndex))
    }

    override fun add(index: Int, element: T) {
        throw Exception("!!")
    }

    override fun addAll(index: Int, elements: Collection<T>): Boolean {
        throw Exception("!!")
    }

    override fun listIterator(index: Int): MutableListIterator<T> {
        return super.listIterator(getIndex(index))
    }

    override fun removeRange(fromIndex: Int, toIndex: Int) {
        super.removeRange(getIndex(fromIndex), getIndex(toIndex))
    }
}
