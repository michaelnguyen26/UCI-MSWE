### USE CONDA INTERPRETER ###
# Tensorflow only supports the 64-bit version of Python 
# Tensorflow only supports Python 3.5 to 3.8 

from keras.datasets import mnist
from keras import models
from keras import layers
from keras.utils import to_categorical


# Step 1: 
# train_images and train_labels form the training set, the data that the model will
# learn from. The model will then be tested on the test set, test_images and test_labels
(train_images, train_labels), (test_images, test_labels) = mnist.load_data()

# Step 2:
# Network consists of a sequence of two Dense layers, which are densely connected (also called fully connected) 
# neural layers. The second (and last) layer is a 10-way softmax layer, which means it will return an array of 10 
# probability scores (summing to 1). Each score will be the probability that the current digit image belongs to
#one of our 10 digit classes
network = models.Sequential()
network.add(layers.Dense(512, activation='relu', input_shape=(28 * 28,)))
network.add(layers.Dense(10, activation='softmax'))


# Step 3:
# loss function, optimizer, and metrics (accuracy) - the fraction of the images that were correctly classified
network.compile(optimizer='rmsprop',
                loss='categorical_crossentropy',
                metrics=['accuracy'])

# Step 4: Reshape the Data
# preprocess the data by reshaping it into the shape the network expects and scaling it so that all values 
# are in the [0, 1] interval
train_images = train_images.reshape((60000, 28 * 28))
train_images = train_images.astype('float32') / 255
test_images = test_images.reshape((10000, 28 * 28))
test_images = test_images.astype('float32') / 255


# Step 5: Categorically Encode the Labels
train_labels = to_categorical(train_labels)
test_labels = to_categorical(test_labels)

# Step 6: Train the Network
network.fit(train_images, train_labels, epochs=5, batch_size=128)

# Step 7: Test Network Model Performs Well on the Test Set Too
test_loss, test_acc = network.evaluate(test_images, test_labels)
print('test_acc:', test_acc)