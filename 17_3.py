# Решение 17.3

# Открываем файл для чтения
f = open('t.txt')

# Преобразуем строки в файле в список целых чисел
sequence_num = list(map(int, f.readlines()))

# Инициализируем переменные
count = 0 # Счетчик количества пар
max_sum = -10000000000
odd_elements = [] # Массив нечетных элементов последовательности

# Проходим по всем элементам последовательности
for i in range(0, len(sequence_num) - 2):
    # Проходимся по каждому элементу нашей "тройки"
    for j in range(3):
        # Если одно из чисел нечетно, то добавляем в массив нечетных чисел нашей "тройки"
        if (sequence_num[i+j] % 2 != 0):
            odd_elements.append(sequence_num[i+j])
   # Проверяем условие: "есть как минимум одно число, начинающееся с цифры 5, а сумма нечетных элементов тройки не меньше 666"
    if ((str(sequence_num[i])[0] == '5' or str(sequence_num[i+1])[0] == '5' or str(sequence_num[i+2])[0] == '5') and (sum(odd_elements) > 666)):
       # Если мы нашли подходящую последовательность:
       count += 1 # Увеличиваем количество найденных пар
       max_sum = max(max_sum, sequence_num[i] + sequence_num[i+1] + sequence_num[i+2]) # Ищем максимальную сумму нашей "тройки"
    odd_elements = [] # Очищаем массив для следующей "тройки", чтобы в массиве всегда были только актуальные три числа

f.close() # Закрываем файл
print(count, max_sum) # Выводим ответ


