from keras.datasets import imdb
from keras import models
from keras import layers
import matplotlib.pyplot as plt
import numpy as np

(train_data, train_labels), (test_data,
                             test_labels) = imdb.load_data(num_words=10000)


def vectorize_sequences(sequences, dimension=10000):
    # Create an all-zero matrix of shape (len(sequences), dimension)
    results = np.zeros((len(sequences), dimension))
    for i, sequence in enumerate(sequences):
        results[i, sequence] = 1.  # set specific indices of results[i] to 1s
    return results


# Our vectorized training data
x_train = vectorize_sequences(train_data)
# Our vectorized test data
x_test = vectorize_sequences(test_data)


# Our vectorized labels
y_train = np.asarray(train_labels).astype('float32')
y_test = np.asarray(test_labels).astype('float32')

model = models.Sequential()
model.add(layers.Dense(16, activation='relu', input_shape=(10000,)))

# Get layers from user (accuracy performs worse on multiple layers by overfitting)
layerInput = int(input("\nHow many layers: "))

# get hidden units (more hidden units (a higher-dimensional representation space)
# allows the network to learn more-complex representations, but it makes the network
# more computationally EXPENSIVE and may lead to learning unwanted patterns)
hiddenUnits = int(input("How many hidden units: "))

for i in range(layerInput):
    model.add(layers.Dense(hiddenUnits, activation='relu'))

# here the SIGMOID function in the output layer squishes the results between 0 and 1
activationInput = str(input("Enter \"sigmoid\" or \"tanh\": "))  # get activation function
model.add(layers.Dense(1, activation='{0}'.format(activationInput)))

print("\nUse \"binary_crossentropy\" or \"mse\"")  # decide which loss function to use
lossType = str(input("Enter choice: "))

model.compile(optimizer='rmsprop',
              loss= '{0}'.format(lossType),  #format as string input for loss function
              metrics=['accuracy'])


x_val = x_train[:10000]
partial_x_train = x_train[10000:]

y_val = y_train[:10000]
partial_y_train = y_train[10000:]

# Returns a history object from fit with 20 epochs
history = model.fit(partial_x_train,
                    partial_y_train,
                    epochs=20,
                    batch_size=512,
                    validation_data=(x_val, y_val))

# use history object from everything that happened during training to validate
history_dict = history.history
history_dict.keys()


acc = history.history['accuracy']
val_acc = history.history['val_accuracy']
loss = history.history['loss']
val_loss = history.history['val_loss']

epochs = range(1, len(acc) + 1)

# "bo" is for "blue dot"
plt.plot(epochs, loss, 'bo', label='Training loss')
# b is for "solid blue line"
plt.plot(epochs, val_loss, 'b', label='Validation loss')
plt.title('Training and validation loss')
plt.xlabel('Epochs')
plt.ylabel('Loss')
plt.legend()

plt.show()


plt.clf()   # clear figure
acc_values = history_dict['accuracy']
val_acc_values = history_dict['val_accuracy']

plt.plot(epochs, acc, 'bo', label='Training acc')
plt.plot(epochs, val_acc, 'b', label='Validation acc')
plt.title('Training and validation accuracy')
plt.xlabel('Epochs')
plt.ylabel('Loss')
plt.legend()
plt.show()


# Train network with only 4 epochs this time
model = models.Sequential()
model.add(layers.Dense(16, activation='relu', input_shape=(10000,)))
model.add(layers.Dense(16, activation='relu'))
model.add(layers.Dense(1, activation='sigmoid'))

model.compile(optimizer='rmsprop',
              loss='binary_crossentropy',
              metrics=['accuracy'])

model.fit(x_train, y_train, epochs=4, batch_size=512)
results = model.evaluate(x_test, y_test)

print("\nResults:", results)
print("\nPrediction:", model.predict(x_test))
