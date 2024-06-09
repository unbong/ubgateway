## Gateway 网关
* 是一个基于Spring webflux实现的简单网关。将自定义的处理器与特定路径记性绑定的方式，将请求转入到处理器的相关处理当中。处理器可以通过插件的形式定义，并且当某个请求与链中的某个处理器满足匹配的条件时，处理器中的执行处理功能会被调用。也可以自定义过滤器，过滤器也是链式的结构，目前只能够对所有处理器进行过滤。