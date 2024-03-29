package com.yll.consumer.amqp.listener;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.time.LocalTime;
import java.util.Map;

@Component
@Slf4j
public class SpringRabbitListener {
	// 利用RabbitListener来声明要监听的队列信息
	// 将来一旦监听的队列中有了消息，就会推送给当前服务，调用当前方法，处理消息。
	// 可以看到方法体中接收的就是消息体的内容
	@RabbitListener(queuesToDeclare = @Queue("simple.queue"))
	public void listenSimpleQueueMessage(String msg) {
		System.out.println("spring 消费者接收到消息：【" + msg + "】");
	}

	@RabbitListener(queuesToDeclare = @Queue("work.queue"))
	public void listenWorkQueue1(String msg) throws InterruptedException {
		System.out.println("消费者1接收到消息：【" + msg + "】" + LocalTime.now());
		Thread.sleep(20);
	}

	@RabbitListener(queuesToDeclare = @Queue("work.queue"))
	public void listenWorkQueue2(String msg) throws InterruptedException {
		System.err.println("消费者2........接收到消息：【" + msg + "】" + LocalTime.now());
		Thread.sleep(200);
	}

	@RabbitListener(bindings = @QueueBinding(
			value = @Queue(name = "fanout.queue1"),
			exchange = @Exchange(name = "hmall.fanout", type = ExchangeTypes.FANOUT)
	))
	public void listenFanoutQueue1(String msg) {
		System.out.println("消费者1接收到Fanout消息：【" + msg + "】");
	}

	@RabbitListener(bindings = @QueueBinding(
			value = @Queue(name = "fanout.queue2"),
			exchange = @Exchange(name = "hmall.fanout", type = ExchangeTypes.FANOUT)
	))
	public void listenFanoutQueue2(String msg) {
		System.out.println("消费者2接收到Fanout消息：【" + msg + "】");
	}

	@RabbitListener(bindings = @QueueBinding(
			value = @Queue(name = "direct.queue1"),
			exchange = @Exchange(name = "hmall.direct"),
			key = {"red", "blue"}
	))
	public void listenDirectQueue1(String msg){
		System.out.println("消费者1接收到direct.queue1的消息：【" + msg + "】");
	}

	@RabbitListener(bindings = @QueueBinding(
			value = @Queue(name = "direct.queue2"),
			exchange = @Exchange(name = "hmall.direct"),
			key = {"red", "yellow"}
	))
	public void listenDirectQueue2(String msg){
		System.out.println("消费者2接收到direct.queue2的消息：【" + msg + "】");
	}

	@RabbitListener(bindings = @QueueBinding(
			value = @Queue(name = "topic.queue1"),
			exchange = @Exchange(name = "hmall.topic", type = ExchangeTypes.TOPIC),
			key = "china.#"
	))
	public void listenTopicQueue1(String msg){
		System.out.println("消费者1接收到topic.queue1的消息：【" + msg + "】");
	}

	@RabbitListener(bindings = @QueueBinding(
			value = @Queue(name = "topic.queue2"),
			exchange = @Exchange(name = "hmall.topic", type = ExchangeTypes.TOPIC),
			key = "#.news"
	))
	public void listenTopicQueue2(String msg){
		System.out.println("消费者2接收到topic.queue2的消息：【" + msg + "】");
	}

	@RabbitListener(queuesToDeclare = @Queue("object.queue"))
	public void listenSimpleObjectQueueMessage(Map<String,Object> msg) {
		System.out.println("spring 消费者接收到消息：【" + msg + "】");
	}

}