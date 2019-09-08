## MoreTextView
一行代码实现TextView的“展开”和“收起”
#### 引用
allprojects {
		repositories {
			...
			maven { url 'https://jitpack.io' }
		}
	}
    
    dependencies {
	    implementation 'com.github.xiaolong20190817:MoreTextView:1.0.3'
	}
   
# 使用方法
new UtilMoreText(当前Activity,要操作的TextView,要显示的内容).setLines(缩小显示行数).setSpanTextColor(Color.RED).createString();


