# WhatsThat

Problem Statement:

Remember, how many times, as a tourist, have you felt troubled and frustated about having to
stand in long queues simply to get access to digital assistants, which were old, used headphones, 
sometimes even broken, that did not offer rich and interactive information about a 
landmark/monument, resulting in ruining your experience of visiting the same? WhatsThat! will
cater to the same resulting in a never before experienced encounter for the end user.



Objective and scope of the project:

When people go to tourist attractions and/or landmarks, first thing they look for is a tour guide. 
Which is a person or an audio device or a book that can give the tourists the complete details about the place.
But often it becomes frustrating for the tourist to stand in long queues and wait to purchase the same,
despite shelling out several bucks to procure it.
On an average, a human tourist guide costs around Rs. 300 (whose credibility is not guaranteed),
an AV tourist guide costs around Rs. 200 to Rs.300. The main objective of this project is to convert all android devices
to personal guides by completely eliminating the above mentioned traditional guides with an application by which
users can instantly retrieve the required information about the tourist attractions by simply hovering their device’s camera
towards the landmark. Thus, saving time and money for the visitors.


Process Description:

An Android application that can be
used to detect landmarks. Since Android uses Java
for front-end design, the app’s front-end was coded in Java;
however, image-processing algorithms were implemented by
making native calls to OpenCV functions though the Java
Native Interface (JNI). This ensures faster processing of the
preview frames, while making it trivial to port the algorithm
from a PC implementation to an Android mobile. Also, we
have chosen to work with the ORB (Oriented and Rotated
BRIEF) feature descriptor [2]. ORB is a binary descriptor
which is two orders of magnitude faster to compute than
SIFT, and hence is more reliable for real-time mobile based
applications. It is also more memory efficient, reducing the
memory requirement on smartphone.
