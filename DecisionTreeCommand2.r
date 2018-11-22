# Select high recall term roots >= 0.03

library(rpart)
setwd("C:\\Users\\adn0019\\WORK\\workspace\\eclipse\\defect-proneness-predicton\\data")
jdt = read.csv('jdt_high_recall_termroots.csv', header=TRUE, sep =",")
jdt$buggy = ifelse(jdt$bug==0,0,1)

decision_tree_model = function(formula, training, testing, actual, threshold){
	# STEP 1
	tree = rpart(formula, training, method='class')
	plot(tree)
	text(tree)
	show(tree)
	predictedOutput = predict(tree, testing, type = 'prob')

	# Step 2
	tp = tn = fp = fn = 0
	for (i in 1:length(actual)){
		if (actual[i]==1){ 
			if (predictedOutput[i] >= threshold) # POSITIVE
				tp = tp + 1
			else				# NEGATIVE
				fn = fn + 1
		}else{ 
			if (predictedOutput[i] >= threshold)	# POSITIVE
				fp = fp + 1
			else				# NEGATIVE
				tn = tn + 1
		}
	}
	# Step 3
	precision = tp / (tp + fp)
	accuracy = (tp + tn) / length(predictedOutput)
	recall = tp / (tp + fn) 
	Fscore = 2/(1/precision + 1/recall)
	list("tp"=tp, "tn" = tn, "fp" = fp, "fn" = fn, "precision" = precision, "accuracy" = accuracy, "recall" = recall, "Fscore" = Fscore)
}

tree = decision_tree_model(buggy~bf+loc+noc, jdt, jdt, jdt$buggy, 0.5)