# Group 24 Report

O seguinte relatório tem em vista a análise dos dados recolhidos pelo processamento dos 3 testes de carga.  
A politica optimista da FénixFramework é responsável por atrasar o reconhecimento de erro até ao final, sendo obrigado então a fazer *rollback* nesta eventualidade. Isto tem um impacto negativo, aumentando a latência drasticamente, na existência de muitos conflictos de acesso, tal como poderemos verificar a baixo nas análises feitas pelos subgrupos. 

### 100 Reads

| # | Pedidos | Threads | Throughput | Latência |
|---| ------- | ------- | ---------- | -------- |
| 1 |    1    |   200   |    827.0   |  319.979 |
| 2 |    2    |   100   |    723.8   |  340.533 |
| 3 |    4    |   50    |    978.6   |  267.980 |
| 4 |    50   |    4    |   1031.9   |  265.002 |
| 5 |   100   |    2    |   1090.0   |  250.666 |
| 6 |   200   |    1    |    797.4   |  276.043 |

Nota-se que a melhor configuração será a que tem mais pedidos e menos threas, exatamente 100 pedidos e 2 threads, pois apresenta uma média de latêcia menor.

### 30 Writes

Devido às limitações impostas pelas máquinas à disposição, este teste foi efectuado apenas simulando no máximo 200 utilizadoes (em vez de 2000). Foi então feita a recolha dos seguintes dados com base na variação proporcional entre pedidos e threads.

| # | Pedidos | Threads | Throughput | Latência |
|---| ------- | ------- | ---------- | -------- |
| 1 |    1    |   200   |    81.2    | 232.993  |
| 2 |    2    |   100   |    91.4    |  118.931 |
| 3 |    4    |   50    |    104.6   |  41.438  |
| 4 |    50   |    4    |    98.3    |  15.223  |
| 5 |   100   |    2    |    95.1    |  13.271  |
| 6 |   200   |    1    |   70.0     |  14.516  |

Podemos perceber então que a melhor configuração para evitar situações de conflito será sempre o caso onde apenas temos um utilizador (thread) a correr que pretende efectuar mais pedidos. A melhor configuração para a simulação de número de utilizadores em simultâneo variará com a capacidade do computador e o número de Adventures, Offers, Rooms e Accounts criadas para respectivo process/read.

### 100 Writes

Parâmetros:
1 única conta no módulo Bank
1 única Activity Offer
1 único Hotel com 100 quartos
100 Adventures

#### Resultados

##### 100 users

100 utilizadores em simultâneo, 500 pedidos Process Adventure no total

| Label                    | # Samples | Average | Median | 90% Line | 95% Line | 99% Line | Min |   Max | Errors | Throughtput (Hz) | Received (KB/s) | Sent (KB/s) |
|--------------------------+-----------+---------+--------+----------+----------+----------+-----+-------+--------+------------------+-----------------+-------------|
| Create Bank              |         1 |     132 |    132 |      132 |      132 |      132 | 132 |   132 | 0 %    |             7.57 |           11.40 |        2.49 |
| Create Bank Client       |         1 |      71 |     71 |       71 |       71 |       71 |  71 |    71 | 0 %    |            14.08 |           24.88 |        4.88 |
| Create Bank Account      |         1 |      67 |     67 |       67 |       67 |       67 |  67 |    67 | 0 %    |            14.92 |           22.63 |        6.23 |
| Deposit Bank Account     |         1 |      82 |     82 |       82 |       82 |       82 |  82 |    82 | 0 %    |            12.19 |           17.50 |        2.95 |
| Create Activity Provider |         1 |      85 |     85 |       85 |       85 |       85 |  85 |    85 | 0 %    |            11.76 |           18.22 |        4.03 |
| Create Activity          |         1 |      80 |     80 |       80 |       80 |       80 |  80 |    80 | 0 %    |             12.5 |           24.79 |        4.98 |
| Create Activity Offer    |         1 |     141 |    141 |      141 |      141 |      141 | 141 |   141 | 0 %    |             7.09 |           12.24 |        2.91 |
| Create Hotel             |         1 |      64 |     64 |       64 |       64 |       64 |  64 |    64 | 0 %    |            15.62 |           23.75 |        5.35 |
| Create Hotel Room        |       100 |      97 |     76 |      151 |      185 |      225 |  58 |   342 | 0 %    |            10.28 |           82.25 |        3.68 |
| Create Brokers           |         1 |      78 |     78 |       78 |       78 |       78 |  78 |    78 | 0 %    |            12.82 |           20.53 |        4.40 |
| Create Adventures        |       100 |     148 |     90 |      293 |      366 |      575 |  66 |  1228 | 0 %    |             6.70 |          164.45 |        2.69 |
| Process Adventure        |       500 |    7677 |   3935 |    19898 |    24619 |    39341 |   4 | 49313 | 26 %   |             7.92 |          278.23 |        2.92 |
| TOTAL                    |       709 |    5450 |    588 |    17484 |    22717 |    37646 |   4 | 49313 | 18 %   |             7.97 |          234.20 |        2.97 |
