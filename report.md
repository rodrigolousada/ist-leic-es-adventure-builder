# Group 24 Report

O seguinte relatório tem em vista a análise dos dados recolhidos pelo processamento dos 3 testes de carga.  
A politica optimista da FénixFramework é responsável por atrasar o reconhecimento de erro até ao final, sendo obrigado então a fazer *rollback* nesta eventualidade. Isto tem um impacto negativo, diminuindo o throughput drasticamente, na existância de muitos conflictos de acesso, tal como poderemos verificar a baixo nas análises feitas pelos subgrupos. 

### 100 Reads

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

### 100 Writes
