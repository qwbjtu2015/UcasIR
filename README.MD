搜索引擎——Lucene

1、分支介绍

	master分支:项目主分支（一般情况不做修改）

	development分支:开发分支/记录所有代码开发版本变更

	feature-xxx分支:个人开发分支/每个人开发前从development分支建立自己的开发分支进行修改

2、代码操作git仓库教程

	2.1、克隆项目仓库

		git clone https://git.coding.net/wjh199559/REST-API-management.git # 从远端克隆仓库至本地

		git checkout development # 在本地切换到development分支下

	2.2、从远端的development新建并检出自己的feature分支命名为姓名简写

		git checkout -b feature-xxx development # 从development分支新建并检出feature分支/xxx为姓名简写

	2.3、将自己的代码add/commit至自己的分支即可(重要)/每次提交代码流程如下

		git checkout feature-xxx # 切换到自己的feature分支

		git add . # 上传新文件到本地git仓库

		git commit -m “comment” # 提交修改到本地git仓库/comment为本次提交的注释

		git push origin feature-xxx # 将本地git仓库推送到远端自己的分支

注意:所有代码编写均在自己的feature分支提交/不要修改development分支和master分支