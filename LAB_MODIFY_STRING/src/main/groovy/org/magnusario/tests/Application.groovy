package org.magnusario.tests

class Application {

    static void main(String[] args) {
        String value = "HHello  Worldddd"
        println value
        println modifyString(value)
    }

    /**
     * Метод для удаления дублированных символов из строки
     * Сложность O(n) - всего n итераций, равняющееся количеству символов в строке
     * На каждой итерации от 1 до 2 проверок условия и 1 действие
     * все вызовы методов работают за константное время, такие как проверка на пустоту списка, удаление последнего символа,
     * получение последнего символа или добавление символа в конец
     *
     * Возможно улучшение сложности алгоритма до O(log(n)) не является целесообразным, так как на каждой итерации возрастет
     * количество проверок + функция слияния нескольких символов с возможными коллизиями
     */
    static String modifyString(String value) {
        if (!value)
            return value
        def chars = new LinkedList()
        value.each {
            if (chars.isEmpty())
                chars << it
            else if (chars.last == it)
                chars.removeLast()
            else
                chars << it
        }
        chars.stream().reduce("", (acc, e) -> acc << e)
    }
}
