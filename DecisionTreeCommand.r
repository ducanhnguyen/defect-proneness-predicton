# Select frequent term roots

library(rpart)
setwd("C:\\Users\\adn0019\\WORK\\workspace\\eclipse\\defect-proneness-predicton\\data")
jdt = read.csv('jdt_frequency_termroots.csv', header=TRUE, sep =",")
jdt$buggy = ifelse(jdt$bug==0,0,1)
tree = rpart(buggy~index+folder+cach+type+locat+annot+compil+element+name+attribut+messag+initi+method+constant+field+access+format+oper+enumer+class+variabl+process+base+record+valu+chang+label+resolv+match+byte+handl+structur+evalu+token+updat+work+modifi+option, data=jdt, method='class')
plot(tree)
text(tree)