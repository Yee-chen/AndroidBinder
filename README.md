# AndroidBinder
The demo is about Android Binder


### bindservice

我们都知道服务的启动方式有两种，一种是bindService(),另一种是startService()。
用startService()方式启动的服务就像一匹脱缰的野马，启动后与调用者无关。而bindService()使得调用者与服务端二者之间可以相互交换信息，甚至于调用内部公开方法（能够相互通信）。

bindService()的参数原型如下：

bindService(android.content.Intent, android.content.ServiceConnection, int)

从上面可以看出，使用bindService()方式启动服务时，必须提供一个ServiceConnection对象，该ServiceConnection会监听与服务的连接状态，在连接成功后会回调ServiceConnection::onServiceConnected()方法，向客户端传递与服务通信的IBinder对象。在绑定服务中，多个调用者可同时连接一个服务，只有在第一个调用者建立绑定关系时，系统才会调用服务的onBind()方法，当最后一个客户端与服务取消绑定时，系统才会销毁服务(除非startService()也启动了该服务)。

### 实现绑定服务一般有如下三种方式：

#### Extends Binder Class：
如果服务是应用内部服务，并且在与客户端相同的进程中运行，可以通过继承Binder类来实现绑定服务，在onBind()方法中返回一个该实现类的实例，客户端接收到Binder后，可利用它直接访问Service中的公有方法；
#### Messenger：
如果应用服务需要跨进程工作，那么可以考虑用Messenger为服务创建接口，服务可以按照Message定义不同的Handler,Handler是Messenger的基础，Messenger可与客户端共享一个IBinder，从而使得客户端能利用Message对象向服务发送命令，此外客户端还可以定义自己的Messenger，以方便服务器回传数据。这是执行IPC的最简单方式，Messenger会在单一线程中创建所有请求队列。
#### AIDL：
AIDL执行将对象分解成数据流的工作，操作系统可以识别这些数据流并将它们传递到各进程中，执行IPC过程，AIDL用于服务器需要多线程访问的情况，其不同于Messenger(服务一次只接收一个请求),在AIDL中，服务可以同时执行多个请求，在这种情况下，服务必须具备多线程处理能力，并采用线程安全方式设计(如果你不确定你的服务器必须支持并发操作，请不要使用AIDL，尽量用Messenger或Extends Binder Class代替)。

一定要注意这三种方式所适用的场合。

本Demo实现了以上三种方式的调用端与服务端通信，第一二种方式的客户端与服务端代码见BindServiceDemo，第三种方式的客户端代码见BindServiceDemo服务端代码见AIDLServiceDemo。
