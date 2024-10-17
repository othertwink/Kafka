**Саммари:**
Нагрузкой считаются запросы к эндпоинту. Топики разделены на три партиции каждый, значение тянется из application.yml
Распределение нагрузки: сообщениям не задаются ключи, поэтому kafka автоматически распределяет их по принципу round-robin. Консюмеры настроены на setConcurrency(pratitionsCount)

**Тест:**
1) Кафка поднимается из docker-compose
2) JMeter 1000 threads 0.1 ramp-up отправляет на эндпоинт OnlineOrders http://localhost:8080/api/v1/orders/ POST с телом заказа {"id":1,"price":"100.50","status":"CREATED"}
3) Мониторинг нагрузки через kafka-consumer-groups --describe:
   _docker exec kafka kafka-consumer-groups --describe --group payment_group --bootstrap-server localhost:9092_

