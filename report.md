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
5 pedidos "Process Adventure" por cada Adventure

Sistema:  
CPU: Intel Core i7-5700HQ @ 2.70GHz  
Cores físicos: 4  
Cores lógicos: 8  
RAM: 8 GiB  
Armazenamento: Hitachi HTS721010A9 (7200 rpm HDD)  
Sistema de ficheiros: Btrfs/LUKS/LVM (AES-256)  
SO: Debian GNU/Linux amd64 (stretch/testing)  
DB: MariaDB 10.1.22

#### Resultados
Os dados abaixo são referentes apenas à fase de "Process Adventure"

| Threads | req/thr |   Avg | Min |   Max | Errors | Throughtput (Hz) |
|---------|---------|-------|-----|-------|--------|------------------|
|     500 |       1 |  9980 | 350 | 31558 | 70 %   |             15.5 |
|     100 |       5 | 10482 | 119 | 56631 | 8 %    |              7.9 |
|      50 |      10 |  4149 | 186 | 27316 | 0 %    |             10.4 |
|      10 |      50 |  1207 | 133 |  8020 | 0 %    |              8.0 |
|      10 |      50 |  1149 |  76 | 12866 | 0 %    |              8.4 |
|       5 |     100 |   725 |  83 |  5379 | 0 %    |              6.7 |
|       4 |     125 |   483 |  75 |  2434 | 0 %    |              8.1 |
|       2 |     250 |   263 | 275 |  2544 | 0 %    |              7.5 |
|       1 |     500 |   148 |  75 |  1432 | 0 %    |              6.5 |

Observações:  
- A latência está inversamente relacionada com o número de utilizadores concorrentes.  
- É observada a ocorrência de erros para um número muito elevado de utilizadores concorrentes.  
- O throughtput não parece variar de forma significativa, excepto na ocorrência de uma taxa de erros elevada.  
