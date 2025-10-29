# Cálculo Concorrente de Estatísticas de um Array

## Visão geral
Este programa realiza o cálculo dos valores mínimo e máximo, média e desvio padrão de um grande *array* de números 
(gerados aleatoriamente) de forma concorrente. Cada unidade de processamento disponível é responsável por computar 
esses valores para uma porção do *array* (*slice*), de modo que os resultados parciais são posteriormente agregados
a fim de se obter os valores finais.

Especificamente, cada *slice* é processado por um objeto `Callable` submetido à execução por um objeto do tipo 
`ExecutorService`, o qual pode ser um *fixed thread pool* ou um *cached thread pool*. Os resultados parciais computados 
são materializados como objetos `Future` para serem agregados.

Como saída, o programa exibe:
- os valores computados;
- o tempo despendido, em milissegundos, para realizar as tarefas, e;
- o ganho de desempenho (*speed-up*) considerando como *baseline* a computação realizada por uma única *thread*.

## Tarefas
O código-fonte provido neste repositório está incompleto, sendo necessário implementar os pontos 
indicados com o marcador ``// TODO`` nas seguintes classes:

### 1. Classe [`tasks.StatsTask`](src/tasks/StatsTask.java)
Implementar, no método `call()`, a computação dos valores mínimo e máximo, média, desvio padrão e tempo despendido 
para o conjunto de elementos que está sendo processado.

*Dica:* iterar sobre *slice* que está sendo processado, acumular os valores e retornar uma 
instância da classe [`model.Stats`](src/model/Stats.java), a qual apenas serve para agregar todos esses valores na forma de
um único objeto.

### 2. Classe [`core.StatsRunner`](src/core/StatsRunner.java)
1. Criar exatamente $n$ tarefas, sendo $n$ o valor da constante $CORES$ definida na classe [`config.Config`](src/config/Config.java). Uma tarefa consiste na computação dos valores para um *slice* de tamanho aproximado $L / CORES$, sendo $L$ o tamanho do 
*array*, e o seu resultado é armazenado em uma lista de objetos do tipo [`model.Stats`](src/model/Stats.java).
2. Utilizar o método `invokeAll` do *thread pool* para executar todas as tarefas a ele submetidas de uma única vez e 
coletar os resultados como objetos `Future<Stats>`. 
3. Em seguida, iterar sobre esse conjunto de objetos, invocando o método `get()` em cada um deles para obter o seu valor.
4. Fazer a agrregação (*merge*) dos resultados parciais 
utilizando o método `plus` definido na classe [`model.Stats`](src/model/Stats.java).
5. **Extra:** Obter o tamanho do *thread pool* para o *cached thread pool* e modificar a saída do programa para que esse número seja também exibido.
## Resultado esperado
```
=== Concurrent Array Statistics ===
n = 20,000,000  cores = 11  tasks = 11

Pool      | min     | max       | mean      | stddev    | time (ms)
----------|---------|-----------|-----------|-----------|----------
single    |    0.02 | 999999.99 | 499878.32 | 288680.18 | 18.41
fixed(11) |    0.02 | 999999.99 | 499878.32 | 288680.18 | 7.24
cached    |    0.02 | 999999.99 | 499878.32 | 288680.18 | 3.18

Speedup vs single: fixed = 2.54x, cached = 5.78x
```

## Estrutura do repositório
```
src/main/java/
├─ App.java
├─ config/
│  ├─ Config.java
│  └─ package-info.java
├─ core/
│  ├─ StatsRunner.java
│  └─ package-info.java
├─ model/
│  ├─ PartialStats.java
│  └─ package-info.java
└─ tasks/
   ├─ StatsTask.java
   └─ package-info.java
```