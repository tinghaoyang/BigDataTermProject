import numpy as np
import pandas as pd
import nltk
import string
import re
import os
import codecs
from sklearn import feature_extraction
from sklearn.feature_extraction.text import TfidfVectorizer



stemmer = nltk.stem.porter.PorterStemmer()
remove_punctuation_map = dict((ord(char), None) for char in string.punctuation)

def stem_tokens(tokens):
    return [stemmer.stem(item) for item in tokens]

'''remove punctuation, lowercase, stem'''
def normalize(text):
    return stem_tokens(nltk.word_tokenize(text.lower().translate(remove_punctuation_map)))

vectorizer = TfidfVectorizer(tokenizer=normalize, stop_words='english')

def cosine_sim(text1, text2):
    tfidf = vectorizer.fit_transform([text1, text2])
    return ((tfidf * tfidf.T).A)[0,1]

a = 'We demonstrate the equivalence of Virasoro constraints imposed on continuumlimit of partition function of Hermitean 1 matrix model and the Ward identitiesof Kontsevich s model Since the first model describes ordinary d 2 quantumgravity while the second one is supposed to coincide with Witten s topologicalgravity the result provides a strong implication that the two models areindeed the same'





authors = []
contents = []

with open("result_min", "r") as lines:
    for line in lines:
        authors.append(line.split(',')[0])
        contents.append(line.split(',')[1])

#print len(authors)
#print len(contents)

stopwords = nltk.corpus.stopwords.words('english')
#print stopwords[:10]
from nltk.stem.snowball import SnowballStemmer
stemmer = SnowballStemmer("english")


def tokenize_and_stem(text):
    # first tokenize by sentence, then by word to ensure that punctuation is caught as it's own token
    tokens = [word for sent in nltk.sent_tokenize(text) for word in nltk.word_tokenize(sent)]
    filtered_tokens = []
    # filter out any tokens not containing letters (e.g., numeric tokens, raw punctuation)
    for token in tokens:
        if re.search('[a-zA-Z]', token):
            filtered_tokens.append(token)
    stems = [stemmer.stem(t) for t in filtered_tokens]
    return stems


def tokenize_only(text):
    # first tokenize by sentence, then by word to ensure that punctuation is caught as it's own token
    tokens = [word.lower() for sent in nltk.sent_tokenize(text) for word in nltk.word_tokenize(sent)]
    filtered_tokens = []
    # filter out any tokens not containing letters (e.g., numeric tokens, raw punctuation)
    for token in tokens:
        if re.search('[a-zA-Z]', token):
            filtered_tokens.append(token)
    return filtered_tokens


totalvocab_stemmed = []
totalvocab_tokenized = []
for i in contents:
    allwords_stemmed = tokenize_and_stem(i) #for each item in 'synopses', tokenize/stem
    totalvocab_stemmed.extend(allwords_stemmed) #extend the 'totalvocab_stemmed' list
    
    allwords_tokenized = tokenize_only(i)
    totalvocab_tokenized.extend(allwords_tokenized)

vocab_frame = pd.DataFrame({'words': totalvocab_tokenized}, index = totalvocab_stemmed)
#print 'there are ' + str(vocab_frame.shape[0]) + ' items in vocab_frame'


#print vocab_frame.head()
from sklearn.feature_extraction.text import TfidfVectorizer

#define vectorizer parameters
tfidf_vectorizer = TfidfVectorizer(max_df=0.8, max_features=200000,
                                 min_df=0.1, stop_words='english',
                                 use_idf=True, tokenizer=tokenize_and_stem, ngram_range=(1,3))

tfidf_matrix = tfidf_vectorizer.fit_transform(contents) #fit the vectorizer to synopses

#print(tfidf_matrix.shape)

terms = tfidf_vectorizer.get_feature_names()

from sklearn.metrics.pairwise import cosine_similarity
dist = 1 - cosine_similarity(tfidf_matrix)

from sklearn.cluster import KMeans

num_clusters = 5

km = KMeans(n_clusters=num_clusters)

km.fit(tfidf_matrix)

clusters = km.labels_.tolist()


from sklearn.externals import joblib
joblib.dump(km,  'doc_cluster.pkl')
km = joblib.load('doc_cluster.pkl')
clusters = km.labels_.tolist()

films = { 'authors': authors,'contents': contents, 'clusters': clusters }

frame = pd.DataFrame(films, index = [clusters] , columns = [ 'authors', 'clusters', 'contents'])

#print frame['clusters'].value_counts()



#print("Top terms per cluster:")
#print('----------')
#sort cluster centers by proximity to centroid
order_centroids = km.cluster_centers_.argsort()[:, ::-1] 
maxSim = 0
max_cluster = 0
max_authors = []

for i in range(num_clusters):
    temp = ""
    #print("-----Cluster %d words:" % i)
    for ind in order_centroids[i, :100]: #replace 6 with n words per cluster
        temp += vocab_frame.ix[terms[ind].split(' ')].values.tolist()[0][0].encode('utf-8', 'ignore')
        temp += " "
    #print(' %s' % temp)
    #print a
    #print cosine_sim(a, temp)
    if cosine_sim(a, temp) > maxSim:
        maxSim = cosine_sim(a, temp)
        max_cluster = i
        max_authors = frame.ix[i]['authors']

    
    #print("-----Cluster %d titles:" % i)
    #print(frame.ix[i]['authors'])
    
#print max_cluster
for max_author in max_authors:
    for i in max_author.replace('Authors:', '').split('and'):
        print i

#print('----------')
