# Input: a document + num of topics
# ouput: topics + words
# method: use LDA

library(lda)
library(RTextTools)
library(stringr)
library(tm)
library(NLP)
library(splus2R)

stemmer = function(document){
	# get all words in the document
	words_list = str_extract_all(document, "[a-zA-Z]+")

	# convert list to vector
	words_vector = unlist(words_list, use.names=FALSE)

	# stemming, .e.g., "loves loving lover loved lover" -> "love"  "love"  "lover" "love"  "lover"
	stemming = wordStem(words_vector)

	# rewrite the document
	stemming_document = ''
	for (i in 1:length(stemming))
		stemming_document = paste(stemming_document, stemming[i])
	list("document" = stemming_document)
}

myLda = function(document, numTopic = 4){
	# lexicalize: This function reads raw text in doclines format and returns a corpus and vocabulary suitable for the inference procedures defined in the lda package.
	corpus = lexicalize(document)

	# build model
	set.seed(100)
	
	# lda.collapsed.gibbs.sampler: These functions use a collapsed Gibbs sampler to fit three different models: latent Dirichlet allocation (LDA), the mixed-membership stochastic blockmodel (MMSB), and supervised LDA (sLDA).
	model = lda.collapsed.gibbs.sampler(corpus$document, numTopic, corpus$vocab, 1000, 0.01, 0.01)
	list("ldaModel" = model)
}

stopwordsRemoval = function(document){
	document = lowerCase(document)
	stopwords_regex = paste(stopwords('en'), collapse = '\\b|\\b')
	stopwords_regex = paste0('\\b', stopwords_regex, '\\b')
	document = stringr::str_replace_all(document, stopwords_regex, '')
	list("document" = document)
}

raw.text = readLines("C:\\Users\\adn0019\\Desktop\\jdt\\1.0.txt")
raw.text = stopwordsRemoval(raw.text)$document
raw.text = stemmer(raw.text)$document
lda = myLda(raw.text)
#predictions <- predictive.distribution(lda$ldaModel$document_sums, lda$ldaModel$topics, 0.1, 0.1)
top.topic.words(lda$ldaModel$topics, 5,  by.score = TRUE)