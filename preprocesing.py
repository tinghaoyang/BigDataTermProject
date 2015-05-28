import os
import jieba
import jieba.posseg as pseg


for path in range(1992, 2004):
	path = str(path) + '/'
	for file in os.listdir(path):
		f = open(path + file,'r+')
		x = f.read()
		f.close()

		authors = x[x.find('Authors'):].split('\n')[0]
		print authors
		content = x.split('\\')[4]

		seg_list = jieba.cut(content, cut_all=True)
		result = []
		for seg in seg_list :
		    seg = ''.join(seg.split())
		    if (seg != '' and seg != "\n" and seg != "\n\n") :
		        result.append(seg)

		print result

		f = open('result', 'a+')
		f.write(authors)
		f.write(',')
		f.write(' '.join(result))
		f.write('\n')
		f.close()


